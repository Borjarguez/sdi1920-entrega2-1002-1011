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
            res.redirect("/signup" + "?mensaje=Las contraseñas deben ser iguales&tipoMensaje=alert-danger");
            return;
        }

        if (req.body.email === "") {
            res.redirect("/signup" + "?mensaje=Email vacio&tipoMensaje=alert-danger");
            return;
        }

        if (req.body.name === "" || req.body.surname === "") {
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
                res.redirect("/signup" + "?mensaje=El email ya existe en el sistema&tipoMensaje=alert-danger");
            } else {
                gestorBD.insertUser(user, function (id) {
                    if (id == null)
                        res.redirect("/signup" + "?mensaje=Email o password incorrecto&tipoMensaje=alert-danger");
                    else {
                        req.session.usuario = user.email;
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
                req.session.error = null;
                res.redirect('/home');
            }
        });
    });

    app.get('/logout', function (req, res) {
        req.session.usuario = null;
        res.redirect("/login");
    });

    app.get("/listUsers", function (req, res) {
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

                    let response = swig.renderFile('views/listUsers.html', {
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
                res.send(response);
            }
        });
    });

    app.get("/reset", function (req, res) {
        var encryptedAdmin = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update("admin").digest('hex');
        var encryptedUser = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update("123456").digest('hex');
        var f = new Date();
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
            ]
        };
        gestorBD.resetearBD(datosIniciales, function () {
            res.send("Base de datos reseteada");
        });
    });


};
