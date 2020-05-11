// Módulos
let express = require('express');
let app = express();

let rest = require('request');
app.set('rest', rest);

app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    next();
});

var jwt = require('jsonwebtoken');
app.set('jwt', jwt);

let fs = require('fs');
let https = require('https');

let log4js = require('log4js');
//loggerlevel no es necesario
log4js.configure({
    appenders: { sdi: { type: 'file', filename: 'sdiLog.log' } },
    categories: { default: { appenders: ['sdi'], level: 'trace' } }
});
let logger = log4js.getLogger('sdi');
app.set('logger', logger);


let expressSession = require('express-session');
app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

let crypto = require('crypto');

let fileUpload = require('express-fileupload');
app.use(fileUpload());

let mongo = require('mongodb');
let swig = require('swig');
let bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

let gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app, mongo);

// routerUsuarioToken
var routerUsuarioToken = express.Router();
routerUsuarioToken.use(function (req, res, next) {
    var token = req.headers['token'] || req.body.token || req.query.token;
    if (token != null) {
        jwt.verify(token, 'secreto', function (err, infoToken) {
            if (err || (Date.now() / 1000 - infoToken.tiempo) > 240) {
                res.status(403);
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                return;
            } else {
                res.usuario = infoToken.usuario;
                next();
            }
        });
    } else {
        res.status(403); // Forbidden
        res.json({
            acceso: false,
            mensaje: 'No hay Token'
        });
    }
});

// Aplicar routerUsuarioToken
app.use('/api/chat', routerUsuarioToken);
app.use('/api/amigos', routerUsuarioToken);

// routerUsuarioSession
let routerUsuarioSession = express.Router();
routerUsuarioSession.use(function (req, res, next) {
    let logger = app.get('logger');
    if (req.session.usuario) {
        next();
    } else {
        logger.error("No se encuentra ningún usuario en sesión");
        res.redirect("/login");
    }
});



//Aplicar routerUsuarioSession
app.use("/home", routerUsuarioSession);
app.use("/listUsers", routerUsuarioSession);
app.use("/peticiones", routerUsuarioSession);
app.use("/amigos", routerUsuarioSession);

app.use(express.static('public'));

// Variables
app.set('port', 8081);
app.set('db', 'mongodb://admin:sdi@tiendamusica-shard-00-00-divfp.mongodb.net:27017,tiendamusica-shard-00-01-divfp.mongodb.net:27017,tiendamusica-shard-00-02-divfp.mongodb.net:27017/test?ssl=true&replicaSet=tiendamusica-shard-0&authSource=admin&retryWrites=true&w=majority');
app.set('clave', 'abcdefg');
app.set('crypto', crypto);

//Rutas/controladores por lógica
require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rpeticiones.js")(app, swig, gestorBD);
require("./routes/rapiusuarios.js")(app,gestorBD);

app.get('/', function (req, res) {
    res.redirect('/index');
});

app.use(function (err, req, res, next) {
    console.log("Error producido: " + err); // mostramos el error en consola

    if (!res.headerSent) {
        res.status(400);
    }
});

// Lanzar el servidor
https.createServer({
    key: fs.readFileSync('certificates/alice.key'),
    cert: fs.readFileSync('certificates/alice.crt')
}, app).listen(app.get('port'), function () {
    console.log("Servidor activo: puerto " + app.get('port'));
});