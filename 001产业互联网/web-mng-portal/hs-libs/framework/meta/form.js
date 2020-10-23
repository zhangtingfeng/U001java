import Vue from "vue"
import progress from "@c/progress"
import msg from "@c/msg"
import ajax from "@c/ajax"

let DataFormProtocol = {
	init() {
		// default row
		let row = {}
		for (let f of this.meta.fields)
			row[f.name] = ""
		this.defaultRow = row

		// 表单数据
		this.form = {}
		// 表单操作
		this.formActions = ["save", "cancel"]
		// 列表状态
		this.rowState = {
			selected: []
		}
	},
	searchCreate() {
		this.form = $.extend({}, this.defaultRow, this.search)
		this.form.$op = 1
		this.formEnter()
	},

	listEdit() {
		this.form = $.extend({}, this.defaultRow, this.data.data[this.rowState.active])
		this.formEnter()
	},

	formEnter() {
		if (this.$v) this.$v.reset()
		Vue.set(this, "state", 2)
		Vue.nextTick(() => {
			$(window).resize();
		})
	},

	formCancel() {
		Vue.set(this, "state", 1)
		Vue.nextTick(() => {
			$(window).resize();
		})
	},

	formPostData() {
		let postData = {}
		let form = {}
		for (let f in this.form)
			if ("" != this.form[f]) form[f] = this.form[f]
		postData[this.meta.name] = [form]
		return postData
	},
	formSave() {
		//处理没有权限的功能不能提交,暂时不处理
		// let auth = this.meta && this.meta.auth ? this.meta.auth.edit : ''
		// if( !auth ){
		// 	msg.info("servererror", "error")
		// 	return false;
		// }
		//处理数组转字符串
		for (let f in this.form) {
			if (f != '$op') {
				let value = $("#" + f).val();
				this.form[f] = value ? value : this.form[f];
			}
			if (typeof (this.form[f]) == 'object') {
				this.form[f] = $("#" + f).val();
				this.form[f] = this.form[f].join(",");
			}
		}
		return this.$v.validateAll().then((rs) => {
			if (rs) {
				return this.formSave2()
			}
		})
	},

	formSave2() {
		let postData = this.formPostData()
		progress.show()
		let promise = ajax.post({
			url: `${this.meta.url}/update`,
			data: postData
		})

		promise.then(data => {
			let d = data[this.meta.name][0]
			//处理批量新增的填写数据
			if (d.length >= 1) {
				for (let i = 0; i < d.length; i++) {
					if (this.form.$op == 1) {
						if (!this.data.data) this.data.data = []
						this.data.data.push(d[i])
					} else {
						this.data.data[this.rowState.active] = d[i]
					}
				}
			} else {
				if (this.form.$op == 1) {
					if (!this.data.data) this.data.data = []
					this.data.data.push(d)
				} else {
					this.data.data[this.rowState.active] = d
				}
			}
			this.state = 1
		}).catch((rs) => {
			if (rs && rs.code == "expired")
				msg.info("error.expired", "error")
			else
				msg.info("servererror", "error")
		}).always(() => {
			progress.hide()
		})

		return promise
	}
};

export default class DataForm {
	name = 'DataForm'
	constructor(meta) {
		this.meta = meta
		Object.assign(this, DataFormProtocol)
		this.init()
	}
}