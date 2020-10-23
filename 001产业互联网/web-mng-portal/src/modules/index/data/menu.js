import user from '@m/common/user'

let menutree
let menuobj
let msgs = {
	'en': {
		menus: {}
	},
	'cn': {
		menus: {}
	}
}

export default {
	data: {
		curr: null,
		component: null,
		args: null
	},
	load() {
		let objs = {}
		let datas = []
		for (const m of user.menus) {
			if (!m.icon) m.icon = 'th'
			let o = objs[m.id] = {
				data: m,
				children: []
			}
			if (!m.pid || '0' == m.pid) {
				datas.push(o)
			} else if (objs[m.pid]) {
				objs[m.pid].children.push(o)
				o.parent = objs[m.pid]
			}
			let names = m.name.split('|')
			msgs.en.menus[m.id] = names[1] ? names[1] : names[0]
			msgs.cn.menus[m.id] = names[0]
		}
		menutree = datas
		menuobj = objs
	},
	tree() {
		return menutree
	},
	msgs() {
		return msgs
	},
	get(id) {
		return menuobj[id]
	}
}