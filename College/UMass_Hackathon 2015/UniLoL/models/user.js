var mongoose = require('mongoose');

var userSchema = mongoose.Schema({

	//IRL Info
	firstName: String,
	lastName: String,
	email: String,
	college: String,
	
	//Site profile info
	loginUser: String,
	password: String,
	status: String,

	//In-game info
	ign: String,
	userID: Number,	//summoner ID from Riot Api
	favRole: String,
	favChampion: String,

	//Rank info
	rankSoloQ: {
		tier: Number,
		division: Number,
		lp: Number,
		totalRank: Number
	},
	
	//Admin info
	lastUpdate: Date
});

module.exports = mongoose.model('User', userSchema);