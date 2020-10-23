import $ from 'jquery'
import events, {
	send as eventsSend
} from "./events"

const INFO = "msg-info"
const ALARM = "msg-alarm"
const CONFIRM = "msg-confirm"

let localeProc = null;
function localeMsg(msg) {
	return localeProc ? localeProc(msg) : msg
}
function setLocale(proc) {
	localeProc = proc
}

let MsgInfo = {
	/**
	 * 
	 * @param {*} msg 
	 * @param {*} t: info/error/warning 
	 */
	info(msg, t) {
		msg = localeMsg(msg)
		t = t || "info"
		if (!this.$el) {
			this.$el = $('<div class="msg-info"></div>').hide().click(() => {
				this.hide()
			})
			$("body").append(this.$el)
		}
		this.$el.html(msg).attr("class", "msg-info").addClass("alert-" + t).show()
		if (this.TIMEOUT_HANDLER) {
			window.clearTimeout(this.TIMEOUT_HANDLER)
		}
		this.TIMEOUT_HANDLER = window.setTimeout(() => {
			this.hide()
		}, 5000)
	},
	hide() {
		if (this.$el) {
			this.$el.hide()
		}
	}
}

let MsgConfirm = {
	confirm(msg) {
		msg = localeMsg(msg)

		if (!this.$el) {
			this.$el = $('<div class="modal fade msg-confirm" role="dialog"></div>')
				.html(`
				<div class="modal-dialog modal-sm" role="document">
					<div class="modal-content">
					<div class="modal-body"></div>
					<div class="modal-footer">
					<button type="button" class="btn btn-default" data-result="0">${localeMsg("cancel")}</button>
					<button type="button" class="btn btn-primary" data-result="1">${localeMsg("confirm")}</button>
					</div></div></div>`)
				.modal({
					keyboard: false
				})
		}

		let $this = this
		return new Promise((resolve, reject) => {
			$this.$el.find("button").click(function (event) {
				if (event) {
					event.preventDefault()
				}
				resolve($(this).data("result"))
				$this.$el.modal("hide")
			})

			let $modal = $this.$el.find(".modal-body").html(msg).end().modal('show');
			setTimeout(() => {
				$modal.find("button[data-result=\"0\"]").focus()
			}, 10)
		})
	}
}

let MsgAlarm = {
	alarm(t) {
		t = t || "error"
		if (!this.$el) {
			this.$el = document.createElement("audio")
			document.body.appendChild(this.$el)
		}

		this.$el.src = `audio/${t}.wav`
		this.$el.pause()
		this.$el.load()
		this.$el.play()
	}
}

let obj1 = {
	info: function (msg, t) {
		t = t || "info"
		if (t === "error" || t === "warning") {
			this.alarm(t)
		}
		return MsgInfo.info(msg, t)
	},
	alarm(t) {
		MsgAlarm.alarm(t)
	},
	confirm: function (msg) {
		return MsgConfirm.confirm(msg)
	}
}

let obj2 = {
	info: function (msg, t) {
		eventsSend(INFO, {
			t,
			msg
		})
	},
	alarm(t) {
		MsgAlarm.alarm(t)
	},
	confirm: function (msg) {
		return new Promise((resolve, reject) => {
			eventsSend(CONFIRM, msg, (d) => {
				resolve(d)
			})
		})
	}
}

let curObj = (window.parent === window ? obj1 : obj2)

events(INFO, function (msg) {
	curObj.info(msg.msg, msg.t)
})
events(ALARM, function (t) {
	curObj.alarm(t)
})
events(CONFIRM, function (msg) {
	return curObj.confirm(msg)
})

export default curObj
export { setLocale }