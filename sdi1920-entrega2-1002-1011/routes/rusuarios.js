module.exports = function (app, swig, gestorBD) {

    app.get("/signup", function (req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.get("/index", function (req, res) {
        let respuesta = swig.renderFile('views/index.html', {});
        res.send(respuesta);
    });

    app.post('/signup', function (req, res) {
        if (req.body.password !== req.body.passwordCheck) {
            app.get("logger").error("Error al registrarse, contraseñas diferentes");
            res.redirect("/signup" + "?mensaje=Las contraseñas deben ser iguales&tipoMensaje=alert-danger");
            return;
        }

        if (req.body.email === "") {
            app.get("logger").error("Error al registrarse, email vacio");
            res.redirect("/signup" + "?mensaje=Email vacio&tipoMensaje=alert-danger");
            return;
        }

        if (req.body.name === "" || req.body.surname === "") {
            app.get("logger").error("Error al registrarse, campos vacios");
            res.redirect("/signup" + "?mensaje=Campos vacios&tipoMensaje=alert-danger");
            return;
        }


        let secure = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');

        let user = {
            email: req.body.email,
            name: req.body.name,
            surname: req.body.surname,
            password: secure
        };

        gestorBD.obtenerUsuarios({email: req.body.email}, function (users) {
            if (users.length != 0 && users != null) {
                req.session.usuario = null;
                app.get("logger").error("Error al registrarse, el email ya existe");
                res.redirect("/signup" + "?mensaje=El email ya existe en el sistema&tipoMensaje=alert-danger");
            } else {
                gestorBD.insertUser(user, function (id) {
                    if (id == null) {
                        app.get("logger").error("Error al registrarse, email o contraseña incorrectos");
                        res.redirect("/signup" + "?mensaje=Email o password incorrecto&tipoMensaje=alert-danger");
                    }else {

                        req.session.usuario = user.email;
                        app.get("logger").info("Usuario que se registra: "+req.session.usuario);
                        res.redirect("/home");
                    }
                });
            }
        });
    });

    app.get("/login", function (req, res) {
        if (req.session.error != null) {

            let response = swig.renderFile('views/login.html', {
                errorT: req.session.error
            });
            res.send(response);
        } else {
            let response = swig.renderFile('views/login.html', {});
            res.send(response);
        }
    });

    app.post("/login", function (req, res) {
        let secure = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');

        let criteria = {
            email: req.body.email,
            password: secure
        };

        gestorBD.obtenerUsuarios(criteria, function (users) {
            if (users == null || users.length == 0) {
                req.session.usuario = null;

                res.redirect("/login" + "?mensaje=Email o password incorrecto&tipoMensaje=alert-danger");
            } else {
                req.session.usuario = users[0].email;
                app.get("logger").info("Usuario que inicia sesión: "+req.session.usuario);
                req.session.error = null;
                res.redirect('/home');
            }
        });
    });

    app.get('/logout', function (req, res) {
        app.get("logger").info("Usuario que se va de la sesión: "+req.session.usuario);
        req.session.usuario = null;
        res.redirect("/login");
    });

    app.get("/listaUsuarios", function (req, res) {
            let criteria = {};

            if (req.query.searchText != null)
                criteria = {$or: [
                        {"name": {$regex: ".*" + req.query.searchText + ".*"}},
                        {"surname": {$regex: ".*" + req.query.searchText + ".*"}},
                        {"email": {$regex: ".*" + req.query.searchText + ".*"}}
                ]};

            let pg = parseInt(req.query.pg);

            if (req.query.pg == null) {
                pg = 1;
            }

            gestorBD.obtenerUsuariosPg(criteria, pg, function (users, total) {
                if (users == null) {
                    res.send("Error al listar");
                } else {
                    let lastPg = total / 5;

                    if (total % 5 > 0) lastPg = lastPg + 1;

                    let pages = [];

                    for (let i = pg - 2; i <= pg + 2; i++)
                        if (i > 0 && i <= lastPg) pages.push(i);

                    let response = swig.renderFile('views/listaUsuarios.html', {
                        users: users,
                        pages: pages,
                        actual: pg
                    });

                    res.send(response);
                }
            });
        }
    );

    app.get("/home", function (req, res) {
        let criteria = {}
        gestorBD.obtenerUsuarios(criteria, function (users) {
            if (users == null || users.length == 0)
                res.send("Error al listar");
            else {
                let response = swig.renderFile('views/home.html',
                    {
                        users: users
                    });
                res.send(response);
            }
        });
    });

    app.get("/amigos", function (req, res) {
        let emailUser = req.session.usuario;
        let criterio = {$or: [{"sender": emailUser, "accepted": true}, {"receiver": emailUser, "accepted": true}]};
        let pg = parseInt(req.query.pg);
        if (req.query.pg == null) {
            pg = 1;
        }
        gestorBD.obtenerAmigosPg(criterio, pg, emailUser, function (amigos, total) {
            if (amigos == null) {
                res.send("Error al listar los amigos.")
            } else {
                let lastPg = total / 5;

                if (total % 5 > 0) lastPg = lastPg + 1;

                let pages = [];

                for (let i = pg - 2; i <= pg + 2; i++)
                    if (i > 0 && i <= lastPg) pages.push(i);

                let response = swig.renderFile('views/amigos.html', {
                    amigos: amigos,
                    pages: pages,
                    actual: pg
                });
                app.get("logger").info(req.session.usuario+" : va a la lista de amigos");
                res.send(response);
            }
        });
    });

    app.get("/reset", function (req, res) {
        var encryptedUser = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update("123456").digest('hex');
        var datosIniciales = {
            usuarios: [
                {
                    name: "admin",
                    surname: "admin",
                    email: "admin@email.com",
                    password: encryptedUser
                },
                {
                    name: "Pedro",
                    surname: "Alonso",
                    email: "pedro@uniovi.es",
                    password: encryptedUser
                },
                {
                    name: "Lucas",
                    surname: "Fernandez",
                    email: "lucas@uniovi.es",
                    password: encryptedUser
                },
                {
                    name: "Marta",
                    surname: "Jimenez",
                    email: "marta@uniovi.es",
                    password: encryptedUser
                },
                {
                    name: "Maria",
                    surname: "Perez",
                    email: "maria@uniovi.es",
                    password: encryptedUser
                },
                {
                    name: "Inés",
                    surname: "Andrés",
                    email: "ines@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "Borja",
                    surname: "Rodriguez",
                    email: "borja@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "Prueba",
                    surname: "Prueba",
                    email: "prueba@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "Luisa",
                    surname: "Diaz",
                    email: "luisa@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "sdi",
                    surname: "asignatura",
                    email: "sdi@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "Aurora",
                    surname: "Santos",
                    email: "aurora@uniovi.es",
                    password: encryptedUser
                }, {
                    name: "Antonio",
                    surname: "Garcia",
                    email: "anton@uniovi.es",
                    password: encryptedUser
                }
            ],
            peticiones: [
                {
                    sender: "admin@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: false
                }, {
                    sender: "pedro@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: false
                }, {
                    sender: "lucas@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: false
                }, {
                    sender: "marta@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: false
                }, {
                    sender: "maria@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: false
                }, {
                    sender: "admin@uniovi.es",
                    receiver: "anton@uniovi.es",
                    accepted: false
                }, {
                    sender: "pedro@uniovi.es",
                    receiver: "anton@uniovi.es",
                    accepted: false
                }, {
                    sender: "lucas@uniovi.es",
                    receiver: "anton@uniovi.es",
                    accepted: false
                }, {
                    sender: "marta@uniovi.es",
                    receiver: "anton@uniovi.es",
                    accepted: false
                }, {
                    sender: "maria@uniovi.es",
                    receiver: "anton@uniovi.es",
                    accepted: false
                }
            ],
            amigos: [
                {
                    sender: "ines@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: true
                }, {
                    sender: "borja@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: true
                }, {
                    sender: "luisa@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: true
                }, {
                    sender: "sdi@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: true
                }, {
                    sender: "aurora@uniovi.es",
                    receiver: "prueba@uniovi.es",
                    accepted: true
                }
            ],
            conversaciones : [
                {
                    "senderConver": "prueba@uniovi.es",
                    "receiverConver": "ines@uniovi.es",
                    "mensajes": [
                        {
                            "_id":gestorBD.mongo.ObjectID(),
                            "sender": "prueba@uniovi.es",
                            "receiver": "ines@uniovi.es",
                            "texto": "Hola Inés",
                            "read": true
                        },
                        {
                            "_id":gestorBD.mongo.ObjectID(),
                            "sender": "ines@uniovi.es",
                            "receiver": "prueba@uniovi.es",
                            "texto": "Hola prueba",
                            "read": true
                        },
                        {
                            "_id":gestorBD.mongo.ObjectID(),
                            "sender": "prueba@uniovi.es",
                            "receiver": "ines@uniovi.es",
                            "texto": "¿Como estás?",
                            "read": true
                        },
                        {
                            "_id":gestorBD.mongo.ObjectID(),
                            "sender": "ines@uniovi.es",
                            "receiver": "prueba@uniovi.es",
                            "texto": "Bueno..",
                            "read": true
                        },
                        {
                            "_id":gestorBD.mongo.ObjectID(),
                            "sender": "prueba@uniovi.es",
                            "receiver": "ines@uniovi.es",
                            "texto": "¿Que te pasa?",
                            "read": false
                        }
                    ]
                }
            ]
        };

        gestorBD.resetearBD(datosIniciales, function () {
            app.get("logger").info("Reset de la base de datos");
            res.send("Base de datos reseteada");
        });
    });


};
