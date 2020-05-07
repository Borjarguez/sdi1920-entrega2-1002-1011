module.exports = function (app, gestorBD) {

    app.post("/api/autenticar/", function (req, res) {
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
                var token = app.get('jwt').sign({
                    usuario: criterio.email,
                    tiempo: Date.now() / 1000
                }, "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }
        })
    });

    app.get("/api/amigo", function (req, res) {
        let criteria = {
            $or: [{
                "sender": req.session.usuario,
                "accepted": true
            }, {
                "receiver": req.session.usuario,
                "accepted": true
            }]
        };

        gestorBD.obtenerAmigos(criteria, function (amigos) {
            if (amigos == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                res.status(200);
                res.send(JSON.stringify(amigos));
            }
        });
    });

    app.put("/api/chat/:id", function (req, res) {
        var criteria = {
            "$or": [{
                "receiver": res.usuario
            }, {
                "sender": res.usuario
            }]
        };

        gestorBD.obtenerUsuarios({
            "_id": gestorBD.mongo.ObjectID(req.params.id)
        }, function (user) {
            if (user == null || user[0] == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                gestorBD.obtenerAmigos({
                    "receiver": user[0].email,
                    "accepted": true
                }, function (amigo) {
                    if (amigo == null || amigo[0] == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        });
                    } else {
                        gestorBD.obtenerConversaciones(criteria, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else {
                                if (conversaciones[0] == null) {
                                    var conversacion = {
                                        "sender": res.usuario,
                                        "receiver": amigo[0],
                                        "mensajes": [{
                                            "_id": gestorBD.mongo.ObjectID(),
                                            "sender": res.usuario,
                                            "receiver": amigo[0],
                                            "texto": req.body.text,
                                            "read": false
                                        }]
                                    };

                                    gestorBD.insertarConversacion(conversacion,
                                        function (conversaciones) {
                                            if (conversaciones == null) {
                                                res.status(500);
                                                res.json({
                                                    error: "se ha producido un error"
                                                });
                                            } else {
                                                res.status(201);
                                                res.json({
                                                    mensaje: "conversacion iniciada",
                                                })
                                            }
                                        });
                                } else {
                                    var mensaje = {
                                        "_id": gestorBD.mongo.ObjectID(),
                                        "sender": res.usuario,
                                        "receiver": amigo[0],
                                        "date": new Date(),
                                        "texto": req.body.text,
                                        "read": false
                                    };

                                    gestorBD.insertarMensaje(criterio, {
                                        "$push": {
                                            "mensajes": mensaje
                                        }
                                    }, function (conversaciones) {
                                        if (conversaciones == null) {
                                            res.status(500);
                                            res.json({
                                                error: "se ha producido un error"
                                            });
                                        } else {
                                            res.status(201);
                                            res.json({
                                                mensaje: "mensaje enviado",
                                            })
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    });

    app.get("/api/chat/:id", function (req, res) {
        // TODO revisar porque no me parece que esté bien
        let criteria = {
            "$or": [{"sender": res.usuario}, {"receiver": res.usuario},
                {"sender": req.params.id}, {"receiver": req.params.id}]
        };

        gestorBD.obtenerConversaciones(criteria, function (mensajes) {
            if (mensajes == null) {
                res.status(403);
                res.json({
                    acceso: false,
                    error: 'Acceso denegado'
                });
                return;
            } else {
                res.status(200);
                res.send(JSON.stringify(mensajes));
            }
        });
    });
};