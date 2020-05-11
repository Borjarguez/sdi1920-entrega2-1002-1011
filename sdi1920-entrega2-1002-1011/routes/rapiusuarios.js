module.exports = function (app, gestorBD) {

    app.post("/api/autenticar", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave')).update(req.body.password).digest('hex');

        let criterio = {
            email: req.body.email,
            password: seguro
        };
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                app.get("logger").error("No se puede autenticar en la api");
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
                app.get("logger").info(req.body.email+": se ha autenticado en la api");
                res.json({
                    autenticado: true,
                    token: token
                })
            }
        })
    });

    app.get("/api/amigos", function (req, res) {

        let email = res.usuario;
        let criteria = {$or: [{"sender": email, "accepted": true}, {"receiver": email, "accepted": true}]};

        gestorBD.obtenerAmigos(criteria, email, function (amigos) {
            if (amigos == null) {
                app.get("logger").error(res.usuario+": no puede acceder a la lista de amigos en el servicio web");
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                res.status(200);
                app.get("logger").info(res.usuario+" : va a la lista de amigos en el servicio web");
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
                app.get("logger").error(res.usuario+" : no puede enviar un mensaje en el servidor web");
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                gestorBD.obtenerAmigos(criteria, email, function (amigos) {
                    if (amigos == null || amigos[0] == null) {
                        res.status(500);
                        app.get("logger").error(res.usuario+" : no puede enviar un mensaje en el servidor web");
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
                                {$and: [{"senderConver": email}, {"receiverConver": amigo.email}]},
                                {$and: [{"senderConver": amigo.email}, {"receiverConver": email}]}
                            ]
                        };
                        gestorBD.obtenerConversaciones(criteria, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                app.get("logger").error(res.usuario+" : no puede enviar un mensaje en el servidor web");
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else {
                                if (conversaciones[0] == null) {
                                    let conversacion = {
                                        "senderConver": res.usuario,
                                        "receiverConver": amigo.email,
                                        "mensajes": [{
                                            "_id": gestorBD.mongo.ObjectID(),
                                            "sender": res.usuario,
                                            "receiver": amigo.email,
                                            "texto": req.body.text,
                                            "read": false
                                        }]
                                    };

                                    gestorBD.insertarConversacion(conversacion, function (conversaciones) {
                                        if (conversaciones == null) {
                                            res.status(500);
                                            app.get("logger").error(res.usuario+" : no puede enviar un mensaje en el servidor web");
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
                                        "receiver": amigo.email,
                                        "texto": req.body.text,
                                        "read": false
                                    };

                                    gestorBD.insertarMensaje(criteria, {"$push": {"mensajes": mensaje}}, function (conversaciones) {
                                        if (conversaciones == null) {
                                            res.status(500);
                                            app.get("logger").error(res.usuario+" : no puede enviar un mensaje en el servidor web");
                                            res.json({
                                                error: "se ha producido un error"
                                            });
                                        } else {
                                            res.status(201);
                                            app.get("logger").info(res.usuario+" : ha enviado un mensaje a "+amigo.email);
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
                app.get("logger").error(res.usuario+" : no puede acceder al chat");
                res.json({
                    error: "se ha producido un error"
                });
            } else {
                gestorBD.obtenerAmigos(criteria, email, function (amigos) {
                    if (amigos == null || amigos[0] == null) {
                        res.status(500);
                        app.get("logger").error(res.usuario+" : no puede acceder al chat");
                        res.json({
                            error: "se ha producido un error"
                        });
                    } else {
                        let amigo = null;

                        for (let i = 0; i < amigos.length; i++) {
                            if (amigos[i].email === usuarios[0].email) {
                                amigo = amigos[i]
                            }
                        }

                        criteria = {
                            $or: [
                                {$and: [{"senderConver": email}, {"receiverConver": amigo.email}]},
                                {$and: [{"senderConver": amigo.email}, {"receiverConver": email}]}
                            ]
                        };
                        gestorBD.obtenerConversaciones(criteria, function (conversaciones) {
                            if (conversaciones == null) {
                                res.status(500);
                                app.get("logger").error(res.usuario+" : no puede acceder al chat");
                                res.json({
                                    error: "se ha producido un error"
                                });
                            } else if (conversaciones[0] == null) {
                                res.status(200);
                                app.get("logger").info(res.usuario+" : accedió al chat con "+amigo.email);
                                res.send({});
                            } else {
                                res.status(200);
                                app.get("logger").info(res.usuario+" : accedió al chat con "+amigo.email);
                                res.send(conversaciones[0]);
                            }
                        });
                    }
                });
            }
        });
    });

    app.put("/api/chat/marcarLeido/:id", function (req, res) {


        let email = res.usuario;
        let criteria = {"_id": gestorBD.mongo.ObjectID(req.body.am),
            "mensajes":{"$elemMatch":{"_id":gestorBD.mongo.ObjectID(req.params.id),"read":false,"receiver":email}}
            };

        gestorBD.marcarMensajeLeido(criteria, {$set: {"mensajes.$.read": true}}, function (result) {
            if (result == null) {
                res.status(500);
                app.get("logger").error(res.usuario+" : no puede cambiar un mensaje a leido");
                res.json({
                    error: "se ha producido un error"
                })
            } else {

                res.status(200);
                app.get("logger").error(res.usuario+" : leyo un mensaje");
                res.json({
                    mensaje: "mensaje actualizado a leido"
                })
            }

        });


    });
}