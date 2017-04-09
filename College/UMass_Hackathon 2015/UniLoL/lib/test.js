var riotjs = require('./riot.js');

<<<<<<< HEAD
process.on('uncaughtException', function (err) {
    console.log(err);
});

riotjs.getSummonerID('Ayuu', function(data){
=======

/*riotjs.getSummonerID('Ayuu', function(data){
	console.log(data);
});*/

riotjs.getSummonerInfo(37738212, function(data){
>>>>>>> e9efce418b2dbc0d9890bd211149750d73378542
	console.log(data);
});
