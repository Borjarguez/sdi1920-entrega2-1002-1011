module.exports = function (app, swig, gestorBD) {

    app.get('/peticion/:email', function (req, res) {

        let peticion = {
            sender: req.session.usuario,
            receiver : req.params.email,
            accepted : false
        };
        if (peticion.sender == peticion.receiver){


            res.redirect("/listUsers"+"?mensaje=No te puedes mandar petición a ti mismo&tipoMensaje=alert-danger");

        }

        else {
            //Mirar si ya hay una amistad entre ellos o si ya le ha enviado una petición
            let criterio ={$or:[{"receiver":peticion.sender,"sender":peticion.receiver},
            {"receiver":peticion.receiver,"sender":peticion.sender}]};
            gestorBD.mandarPeticion(criterio,peticion, function (email) {
                if (email == null){
                    res.redirect("/listUsers"+"?mensaje=No se puede mandar petición a este usuario&tipoMensaje=alert-danger");
                }
                else{
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
                res.send(responde);
            }
        });
    });

    app.get('/aceptar/:id', function (req,res) {
        let criterio = { "_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.aceptarPeticion(criterio, function (amigo) {
            if (amigo == null) {
                res.send("Error al aceptar la petición ");
            }else{
                res.redirect("/peticiones");
            }

        })
    });


};
