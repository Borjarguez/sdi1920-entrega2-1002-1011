module.exports = funcapp.get("api/listFriends", function (req, res) {
        let criteria = {};
        let friendsList;

        gestorBD.obtainFriends(criteria, function (friends) {
            if (friends == null || friends.length === 0) {
                res.status(401);
            } else {
                friendsList = friends;
                res.status(200);
                res.send(JSON.stringify(friends));
            }
        })
    });tion (app, gestorBD) {

    app.post("/api/autenticar", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave')).update(req.body.password).digest('hex');

        let criterio = {
            email: req.body.email,
            password: seguro
        };

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401);
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000}, "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }
        })
    });


};