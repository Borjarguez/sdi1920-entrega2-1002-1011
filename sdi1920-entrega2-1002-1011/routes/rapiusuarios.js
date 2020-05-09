module.exports = function (app, gestorBD) {

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

    app.get("/api/chat/amigos", function (req, res) {
        let email = res.usuario;
        let criteria = {$or: [{"sender": email, "accepted": true}, {"receiver": email, "accepted": true}]};

        gestorBD.obtenerAmigos(criteria, email, function (amigos) {
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
        let email = res.usuario;
        let criteria = {$or: [{"sender": email, "accepted": true}, {"receiver": email, "accepted": true}]};

        gestorBD.obtenerUsuarios({"_id": gestorBD.mongo.ObjectID(req.params.id)}, function (usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                gestorBD.obtenerAmigos(criteria, email, function (amigos) {
                    if (amigos == null || amigos[0] == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        });
                    } else {
                        let amigo = null;
                        for (let i = 0; i < amigos.length; i++)
                            if (amigos[i].email === usuarios[0].email)
                                amigo = amigos[i]
                        criteria = {
                            $or: [
                                {$and: [{"sender": email}, {"receiver": amigo.email}]},
                                {$and: [{"sender": amigo.email}, {"receiver": email}]}
                            ]
                        };
                        gestorBD.obtenerConversaciones(criteria, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else {
                                if (conversaciones[0] == null) {
                                    let conversacion = {
                                        "sender": res.usuario,
                                        "receiver": amigo.email,
                                        "mensajes": [{
                                            "_id": gestorBD.mongo.ObjectID(),
                                            "sender": res.usuario,
                                            "receiver": amigo[0],
                                            "texto": req.body.text,
                                            "read": false
                                        }]
                                    };

                                    gestorBD.insertarConversacion(conversacion, function (conversaciones) {
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

                                    gestorBD.insertarMensaje(criterio, {"$push": {"mensajes": mensaje}}, function (conversaciones) {
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
        })

    });

    app.get("/api/chat/:id", function (req, res) {
        let email = res.usuario;
        let criteria = {$or: [{"sender": email, "accepted": true}, {"receiver": email, "accepted": true}]};

        gestorBD.obtenerUsuarios({"_id": gestorBD.mongo.ObjectID(req.params.id)}, function (usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                gestorBD.obtenerAmigos(criteria, email, function (amigos) {
                    if (amigos == null || amigos[0] == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        });
                    } else {
                        let amigo = null;
                        for (let i = 0; i < amigos.length; i++)
                            if (amigos[i].email === usuarios[0].email)
                                amigo = amigos[i]
                        criteria = {
                            $or: [
                                {$and: [{"sender": email}, {"receiver": amigo.email}]},
                                {$and: [{"sender": amigo.email}, {"receiver": email}]}
                            ]
                        };
                        gestorBD.obtenerConversaciones(criteria, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else {
                                res.status(200);
                                res.send(JSON.stringify(conversaciones));
                            }
                        });
                    }
                });
            }
        });
    });
}