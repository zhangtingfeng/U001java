import Vue from "vue"
import VueI18n from 'vue-i18n'
import msgApp from "./app.json"
import msgValidation from "./validation.json"
import $ from "jquery"
import { setLocale as setMsgLocale } from "../msg"
import events, { send2 as sendEvents } from "../events"

Vue.use(VueI18n)

// 处理Vue子控件可以获取父控件的key
Vue.prototype.$$t = Vue.prototype.$t;
Vue.prototype.$t = function (key, ...values) {
	let rs = this.$$t(key, values);
	if (rs === key && this.$parent) {
		rs = this.$parent.$t(key, values);
	}
	return rs;
}

let i18n
let locale = {
	DEFAULT: 'cn',
	langs: {
		'en': 'EN',
		'cn': '中文'
	},
	set(v) {
		window.localStorage.setItem('locale', v)
		i18n.locale = v
		// 发送通知到子frame
		let frames = document.getElementsByTagName("iframe")
		for (let f of frames) {
			if (f.contentWindow) { sendEvents(f.contentWindow, "locale", v) }
		}
	},
	get() {
		let l = window.localStorage.getItem('locale')
		if (!this.langs[l]) {
			l = this.DEFAULT
		}
		return l
	},
	index() {
		let v = this.get()
		let idx = this.langs.findIndex((e) => e == v)
		return idx < 0 ? 0 : idx
	},
	parse(msg) {
		let msgs = msg.split("|")
		return [msgs[0], msgs.length > 1 ? msgs[1] : msgs[0]]
	},
	currMsg(msg) {
		let msgs = this.parse(msg)
		return this.DEFAULT === this.CURRENT ? msgs[0] : msgs[1]
	}
}
locale.CURRENT = locale.get()

let MSGS = $.extend(true, {}, msgApp, msgValidation)
let i18nConfig = {
	locale: locale.get(),
	fallbackLocale: locale.DEFAULT,
	silentTranslationWarn: true,
	messages: MSGS
}
i18n = new VueI18n(i18nConfig)

// 关联消息的多语言处理
setMsgLocale((msg) => {
	return i18n.t(msg)
})

// 关联父窗口的多语言处理
if (window.parent !== window) {
	events("locale", (l) => {
		locale.set(l)
	})

	sendEvents(window.parent, "locale-get", {}, (l) => {
		locale.set(l)
	})
}

// 关联子窗口的多语言处理
events("locale-get", () => {
	return locale.get()
})


export default locale
export {
	i18n
}