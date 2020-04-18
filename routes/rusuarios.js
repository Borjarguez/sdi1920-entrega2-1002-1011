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
            }else{
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
            let respuesta = swig.renderFile('views/login.html', {
                errorT: req.session.error
            });
            res.send(respuesta);
        } else {
            let respuesta = swig.renderFile('views/login.html', {});
            res.send(respuesta);
        }
    });

    app.post("/login", function (req, res) {
        let secure = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');

        let criteria = {
            email: req.body.email,
            password: seguro
        };

        gestorBD.obtenerUsuarios(criteria, function (users) {
            if (users == null || users.length == 0) {
                req.session.usuario = null;

                let errorT = {
                    tipoMensaje: "alert-danger",
                    mensaje: "Email o password incorrecto"
                };

                req.session.error = errorT;
                res.redirect("/login");
            } else {
                req.session.usuario = users[0].email;
                req.session.error = null;
                res.redirect('/publicaciones');
            }
        });
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.send("Usuario desconectado");
    });

    app.get("/error", function (req, res) {
        let respuesta = swig.renderFile('views/error.html', {
            error: "Error"
        });
        res.send(respuesta);
    })
};
