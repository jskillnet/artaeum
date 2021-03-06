const mongoose = require('mongoose')
const autoIncrement = require('mongoose-auto-increment')

autoIncrement.initialize(mongoose.connection)

const commentsSchema = new mongoose.Schema({
  text: {
    type: String,
    minlength: 1
  },
  resourceType: {
    type: String,
    required: true
  },
  resourceId: {
    type: Number,
    required: true
  },
  userId: {
    type: String,
    required: true
  },
  createdDate: {
    type: Date,
    required: true
  }
})

commentsSchema.plugin(autoIncrement.plugin, { model: 'comments' })

module.exports = mongoose.model('comments', commentsSchema)
