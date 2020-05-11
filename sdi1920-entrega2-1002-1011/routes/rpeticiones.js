module.exports = function (app, swig, gestorBD) {

    app.get('/peticion/:email', function (req, res) {

        let peticion = {
            sender: req.session.usuario,
            receiver : req.params.email,
            accepted : false
        };
        if (peticion.sender == peticion.receiver){
            app.get("logger").error(req.session.usuario+" : intenta mandarse una petición así mismo");

            res.redirect("/listUsers"+"?mensaje=No te puedes mandar petición a ti mismo&tipoMensaje=alert-danger");

        }

        else {
            //Mirar si ya hay una amistad entre ellos o si ya le ha enviado una petición
            let criterio ={$or:[{"receiver":peticion.sender,"sender":peticion.receiver},
            {"receiver":peticion.receiver,"sender":peticion.sender}]};
            gestorBD.mandarPeticion(criterio,peticion, function (email) {
                if (email == null){
                    app.get("logger").error(req.session.usuario+" : intenta mandar una petición fallida");
                    res.redirect("/listUsers"+"?mensaje=No se puede mandar petición a este usuario&tipoMensaje=alert-danger");
                }
                else{
                    app.get("logger").info(req.session.usuario+" : manda peticion a "+req.params.email);
                    res.redirect("/listUsers");

                }
            });
        }

    });

    app.get('/peticiones', function(req,res) {
        var criterio = {"receiver":req.session.usuario,"accepted":false};
        let pg = parseInt(req.query.pg);

        if (req.query.pg == null){ pg = 1;}
        gestorBD.obtenerPeticionesPg(criterio,pg, function(peticiones, total) {
            if (peticiones == null){
                res.send("Listing error for request");
            }
            else {
                let lastPg = total / 5;
                if (total % 5 > 0) lastPg = lastPg + 1;
                let pages = [];

                for (let i = pg - 2; i <= pg + 2; i++)
                    if (i > 0 && i <= lastPg) pages.push(i);

                let responde = swig.renderFile('views/peticiones.html', {
                    peticiones : peticiones,
                    pages:pages,
                    actual:pg
                });
                app.get("logger").info(req.session.usuario+" : va a la lista de peticiones");
                res.send(responde);
            }
        });
    });

    app.get('/aceptar/:id', function (req,res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.aceptarPeticion(criterio, function (amigo) {
            if (amigo == null) {
                app.get("logger").error(req.session.usuario+" : error al aceptar una peticion");
                res.send("Error al aceptar la petición ");
            }else{
                app.get("logger").info(req.session.usuario+" : acepta una peticion");
                res.redirect("/peticiones");
            }

        })
    });


};
