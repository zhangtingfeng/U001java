import $ from 'jquery'
import events, {
  send as eventsSend
} from "./events"

const SHOW = "progress-show"
const HIDE = "progress-hide"

let obj1 = {
  show(msg) {
    msg = msg || ""
    if (!module.$el) {
      module.$el = $('<div class="modal progress-modal"></div>')
        .html(`<div class="modal-body"><p><i class="fa fa-circle-o-notch"></i>${msg}</p></div>`)
        .modal({
          keyboard: false
        })
    }
    module.$el.modal('show')
  },
  hide() {
    if (module.$el) {
      module.$el.modal('hide')
    }
  }
}

let obj2 = {
  show(msg) {
    eventsSend(SHOW, msg)
  },
  hide() {
    eventsSend(HIDE)
  }
}

let curObj = (window.parent === window ? obj1 : obj2)

events(SHOW, function (msg) {
  curObj.show(msg)
})
events(HIDE, function (msg) {
  curObj.hide(msg)
})

export default curObj