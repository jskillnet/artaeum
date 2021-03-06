const mongoose = require('mongoose')

const messagesSchema = new mongoose.Schema({
  text: {
    type: String,
    minlength: 1
  },
  sender: {
    type: String,
    required: true
  },
  receiver: {
    type: String,
    required: true
  },
  createdDate: {
    type: Date,
    required: true
  }
})

module.exports = mongoose.model('messages', messagesSchema)
