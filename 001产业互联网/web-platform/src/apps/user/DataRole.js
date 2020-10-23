import {ajax } from "@f/vendor";

let DataRoleProtocol = {
	init() {
		this.formData = {
			config: {
				privileges: []
			},
			data: {
				privileges: []
			},
			data2: {
				privileges: []
			}
		}
	},
	searchCreate() {
		this.form = $.extend({}, this.defaultRow, this.search)
		this.form.$op = 1
		this.formData.data.privileges = []
		this.formData.data2.privileges = []
		if (this.$v) this.$v.reset()
		this.state = 2
	},

	listEdit() {
		this.form = $.extend({}, this.defaultRow, this.data.data[this.rowState.active])
		if (this.$v) this.$v.reset()
		$.when(
			ajax.post({
				url: 'api/platform/meta/role-menu/query',
				data: {
					roleId: this.form.id
				}
			}),
			ajax.post({
				url: 'api/platform/meta/role-privilege/query',
				data: {
					roleId: this.form.id
				}
			})
		).then((data1, data2) => {
			this.formData.data.privileges = []
			this.formData.data2.privileges = []
			for (let d of data1['role-menu'] || []) {
				this.formData.data.privileges.push(d.menuId)
				this.formData.data2.privileges.push(d.menuId)
			}
			for (let d of data2['role-privilege'] || []) {
				this.formData.data.privileges.push(d.privilegeId)
				this.formData.data2.privileges.push(d.privilegeId)
			}
			this.state = 2
		})
	},

	formPostData() {
		let postData = {}
		postData[this.meta.name] = [this.form]
		let menus = []
		let privileges = []
		for (let m of this.formData.data.privileges) {
			let idx = this.formData.data2.privileges.indexOf(m)
			if (idx > -1) { this.formData.data2.privileges.splice(idx, 1) }

			let p = this.formData.config.objs[m]
			if (p.t === 'm') {
				menus.push({ menuId: m, $op: 0 })
			} else if (p.t === 'p') {
				privileges.push({ privilegeId: m, $op: 0 })
			}
		}
		for (let m of this.formData.data2.privileges) {
			let p = this.formData.config.objs[m]
			if (!p || p.t === 'm') {
				menus.push({ menuId: m, $op: -1 })
			} else if (p.t === 'p') {
				privileges.push({ privilegeId: m, $op: -1 })
			}
		}
		postData['role-menu'] = menus
		postData['role-privilege'] = privileges
		return postData
	}
}

export default class DataRole {
	constructor(meta) {
		this.meta = meta
		Object.assign(this, DataRoleProtocol)
		this.init()
	}
}
