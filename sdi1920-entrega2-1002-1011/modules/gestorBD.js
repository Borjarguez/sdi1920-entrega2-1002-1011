module.exports = {
    mongo: null,
    app: null,

    init: function (app, mongo) {
        this.mongo = mongo;
        this.app = app;
    },

    insertUser: function (usuario, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('usuarios');
                collection.insert(usuario, function (err, result) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(result.ops[0]._id);
                    }
                    db.close();
                });
            }
        });
    },

    obtainUsers: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('usuarios');
                collection.find(criterio).toArray(function (err, usuarios) {
                    if (err) {
                        funcionCallback(null);
                    } else {
                        funcionCallback(usuarios);
                    }
                    db.close();
                });
            }
        });
    },

    obtainUsersPg: function (criteria, pg, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {
                funcionCallback(null);
            } else {
                let collection = db.collection('usuarios');
                collection.count(function (err, count) {
                    collection.find(criteria).skip((pg - 1) * 5).limit(5).toArray(function (err, users) {
                        if (err) {
                            funcionCallback(null);
                        } else {
                            funcionCallback(users, count);
                        }
                        db.close();
                    });
                });
            }
        });
    },
    mandarPeticion: function (criterio,peticion, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err){
                funcionCallback(null);
            }else{

                let collection = db.collection('peticiones');
                collection.find(criterio).toArray(function (err, peticiones) {
                    if (err || peticiones.length > 0) {
                        funcionCallback(null);
                    } else {
                        collection.insert(peticion, function (err, result) {
                            if (err) {
                                funcionCallback(null);
                            } else {
                                funcionCallback(result.ops[0].sender);
                            }
                            db.close();
                        });

                    }

                });
            }

        });
    },
    obtenerPeticionesPg: function (criterio, pg, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            var collection = db.collection('peticiones');
            collection.find(criterio).count(function (err, count) {
                collection.find(criterio).skip((pg - 1) * 5).limit(5)
                    .toArray(function (err, peticiones) {
                        if (err) {
                            funcionCallback(null);
                        } else {
                            funcionCallback(peticiones, count);
                        }
                        db.close();
                    });
            });
        })
    },
    aceptarPeticion: function (criterio, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            var collection = db.collection('peticiones');
            collection.findOne(criterio, function (err, peticion) {
                if (err){
                    funcionCallback(null);
                }else {
                    collection.update(criterio, {$set: {"accepted": true}}, function (err, peticion) {
                        if (err){
                            funcionCallback(null);
                        }else {
                            funcionCallback(peticion);
                        }
                    });
                }
            });
        });
    },
    obtenerAmigosPg: function (criterio, pg,emailUsuario, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            let collection = db.collection('peticiones');
            let collectionU = db.collection('usuarios');

            collection.find(criterio).toArray(function (err, peticiones) {

                let i = 0;
                let emails = [];
                for (i = 0; i < peticiones.length; i++) {
                    let peticion = peticiones[i];
                    if (peticion.sender == emailUsuario){
                        emails.push(peticion.receiver);
                    }
                    else if (peticion.receiver == emailUsuario){
                        emails.push(peticion.sender);
                    }
                }

                criterio = {"email":{$in:emails}};
                collectionU.find(criterio).count(function (err, count) {
                    collectionU.find(criterio).skip((pg - 1) * 5).limit(5)
                        .toArray(function (err, amigos) {
                        if (err) {
                            funcionCallback(null);
                        }else{
                            funcionCallback(amigos,count);
                        }
                    });
                });

            });
        });
    },

    resetearBD: function (datosIniciales, funcionCallback) {
        this.mongo.MongoClient.connect(this.app.get('db'), function (err, db) {
            if (err) {

            } else {
                var collection = db.collection('usuarios');
                collection.deleteMany({}, function (err, result) {
                    var collection = db.collection('peticiones');
                    collection.deleteMany({}, function (err, result) {
                            var collection = db.collection('usuarios');
                            collection.insertMany(datosIniciales.usuarios).then(result => {
                                var collection = db.collection('peticiones');
                                collection.insertMany(datosIniciales.peticiones).then(result => {
                                    collection.insertMany(datosIniciales.amigos).then(result=>{
                                        db.close();
                                        funcionCallback();
                                    });



                            });
                        });
                    });
                });
            }
        });
    }
};