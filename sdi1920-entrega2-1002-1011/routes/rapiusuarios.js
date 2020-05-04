module.exports = function (app, swig, gestorBD) {

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

    app.get("/api/friend", function (req, res) {
        let criteria = {
            $or: [{
                "sender": req.session.usuario,
                "accepted": true
            } || {
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

            gestorBD.obtenerUsuarios({"_id": gestorBD.mongo.ObjectID(req.params.id)}, function (usuario) {
                if (usuario == null || usuario[0] == null) {
                    res.status(500);
                    res.json({
                        error: "se ha producido un error"
                    });
                } else {
                    gestorBD.obtenerAmigos({"receiver": usuario[0].email, "accepted": true}, function (amigo) {
                        if (amigo == null || amigo[0] == null) {
                            res.status(500);
                            res.json({
                                error: "se ha producido un error"
                            });
                        } else {
                            gestorBD.obtenerConversaciones(criteria, function (conversaciones) {

                            })
                        }
                    })
                }
            });


            gestorBD.obtenerUsuarios({
                    "_id": gestorBD.mongo.ObjectID(req.params.id)
                },
                function (usuarios) {
                    if (usuarios == null || usuarios[0] == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        });
                    } else {
                        gestorBD.obtenerConversaciones(criterio, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else {
                                if (conversaciones[0] == null) {
                                    var conversacion = {
                                        "sender": res.usuario,
                                        "receiver": usuarios[0],
                                        "mensajes": [{
                                            "_id": gestorBD.mongo.ObjectID(),
                                            "sender": res.usuario,
                                            "receiver": usuarios[0],
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
                                        "sender": null,
                                        "receiver": null,
                                        "date": new Date(),
                                        "texto": req.body.text,
                                        "read": false
                                    };
                                    if (ofertas[0].owner = res.usuario) {
                                        mensaje.sender = res.usuario;
                                        mensaje.receiver = conversaciones[0].sender;
                                    } else {
                                        mensaje.sender = res.usuario;
                                        mensaje.receiver = ofertas[0].owner;
                                    }
                                    gestorBD.insertarMensaje(criterio, {
                                            "$push": {
                                                "mensajes": mensaje
                                            }
                                        },
                                        function (conversaciones) {
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
    )
    ;


}
;