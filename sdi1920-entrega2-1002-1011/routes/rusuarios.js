module.exports = function (app, swig, gestorBD) {

    app.get("/usuarios", function (req, res) {
        res.send("ver usuarios");
    });

    app.get("/signup", function (req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.post('/signup', function (req, res) {
        if (req.body.password != req.body.passwordCheck) {
            res.redirect("/signup?message=Both passwords must be equal");
            return;
        }

        if (req.body.email == "") {
            res.redirect("/signup?message=Email cannot be empty");
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

        gestorBD.obtainUsers({email: req.body.email}, function (users) {
            if (users.length != 0 && users != null) {
                req.session.usuario = null;
                res.redirect("/signup?message=Email already registered");
            } else {
                gestorBD.insertUser(user, function (id) {
                    if (id == null) {
                        res.redirect("/signup?message=Error during the register");
                    } else {
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

        gestorBD.obtainUsers(criteria, function (users) {
            if (users == null || users.length == 0) {
                req.session.usuario = null;

                let errorT = {
                    messageType: "alert-danger",
                    message: "Email o password incorrecto"
                };

                req.session.error = errorT;
                res.redirect("/login");
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

            if (req.query.search != null)
                criteria = {"name": {$regex: ".*" + req.query.search + ".*"}};

            let pg = parseInt(req.query.pg);

            if (req.query.pg == null) pg = 1;

            gestorBD.obtainUsersPg(criteria, pg, function (users, total) {
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
        gestorBD.obtainUsers(criteria, function (users) {
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
        let criterio = {"sender":emailUser,"accepted":true};
        let criterio2 = {"receiver":emailUser,"accepted":true};
        let pg = parseInt(req.query.pg);

        if (req.query.pg == null) pg = 1;
        gestorBD.obtenerAmigosPg(criterio, pg, emailUser, function (amigos, total) {
            if (amigos == null){
                res.send("Error al listar los amigos.")
            }else {
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
};
