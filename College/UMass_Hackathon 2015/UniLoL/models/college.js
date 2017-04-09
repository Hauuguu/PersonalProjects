var mongoose = require('mongoose');

var collegeSchema = mongoose.Schema({

	collegeName: String,
	emailDomain: String,
	imageURL: String
});

module.exports = mongoose.model('College', collegeSchema);