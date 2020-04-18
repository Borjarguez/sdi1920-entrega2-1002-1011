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

        console.log("PASE POR AQUI");

        gestorBD.obtainUsers(criteria, function (users) {
            if (users == null || users.length == 0) {
                req.session.usuario = null;

                let errorT = {
                    messageType: "alert-danger",
                    message: "Email o password incorrecto"
                };

                console.log("NO FUNCIONA MELON");
                req.session.error = errorT;
                res.redirect("/login");
            } else {
                console.log("SI QUE FUNCIONA PERO NO");
                req.session.usuario = users[0].email;
                req.session.error = null;
                res.redirect('/home');
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

    app.get("/listUsers", function (req, res) {
            let criteria = {};

            if (req.query.search != null)
                criteria = {"nombre": {$regex: ".*" + req.query.search + ".*"}};

            let pg = parseInt(req.query.pg);

            if (req.query.pg == null) pg = 1;

            gestorBD.obtainUsersPg(criteria, pg, function (users, total) {
                if (users == null) {
                    res.send("Listing error");
                } else {
                    let lastPg = total / 4;

                    if (total % 4 > 0) lastPg = lastPg + 1;

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
};
