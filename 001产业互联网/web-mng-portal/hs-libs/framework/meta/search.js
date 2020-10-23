import Vue from 'vue'
import progress from '@c/progress'
import msg from '@c/msg'
import ajax from '@c/ajax'

let DataSearchProtocol = {
	init() {
		// 0： Inited, 1: List, 2: Form
		this.state = 0
		// 查询条件
		this.search = {}
		// 拼接参数
		{
			let m = /\?(.*)/.exec(window.location.hash)
			if (m) {
				let t = /([^=|^&]+)=([^&]+)/ig
				let rs = ''
				while (rs = t.exec(m[1])) {
					this.search[rs[1]] = rs[2]
				}
			}
		}

		let auth1 = this.meta && this.meta.auth ? this.meta.auth.auth : ''
		let auth2 = this.meta && this.meta.auth ? this.meta.auth.edit : ''

		// 查询按钮
		this.searchBtns = []
		this.searchActions = [{
			name: 'query',
			clz: 'primary',
			icon: 'search',
			auth: auth1
		},
		{
			name: 'exp',
			icon: 'download'
		},
		{
			name: 'create',
			icon: 'plus',
			// clz: 'info'
			auth: auth2
		},
		{
			name: 'del',
			icon: 'times',
			auth: auth2,
			// clz: 'danger',
			disabled: true
		}
		]
		// 页索引
		this.page = 1
		// 页大小
		this.pageSize = 20
		// 列表数据
		this.data = {}
		// 列表操作
		this.listActions = [{
			name: 'edit',
			icon: "edit",
			auth: auth2,
		}
			/*, {
						name: "del",
						icon: "times"
					} */
		]
		// 列表状态
		this.rowState = {
			selected: []
		}
	},
	searchQuery(p) {
		this.page = p || 1
		progress.show()
		for (let f in this.search) {
			if (this.search[f] == '') this.search[f] = undefined
		}
		let promise = ajax.post({
			url: `${this.meta.url}/${this.page}?pageSize=${this.pageSize}`,
			data: this.search
		})

		promise.then(data => {
			this.data = data[this.meta.name]
			this.rowState = {
				selected: []
			}
		}).catch(() => {
			msg.info('servererror', 'error')
		}).always(() => {
			progress.hide()
			Vue.nextTick(() => {
				$(window).resize()
			})
		})

		return promise
	},

	searchDel(confirmed) {
		if (!confirmed) {
			let hasSelected = false
			for (let i = this.data.data.length - 1; i >= 0; i--) {
				if (this.rowState.selected[i]) {
					hasSelected = true
					break;
				}
			}
			if (hasSelected) {
				msg.confirm('delconfirm').then((rs) => {
					if (rs) this.searchDel(true)
				})
			}
		} else {
			let datas = []
			for (let i = this.data.data.length - 1; i >= 0; i--) {
				if (this.rowState.selected[i]) {
					let d = $.extend({}, this.data.data[i], {
						$op: -1
					})
					datas.push(d)
				}
			}

			if (datas.length === 0) {
				return
			}
			progress.show()
			let postData = {}
			postData[this.meta.name] = datas
			let promise = ajax.post({
				url: `${this.meta.url}/update`,
				data: postData
			})

			promise.then(data => {
				let datas = this.data.data
				for (let i = datas.length - 1; i >= 0; i--) {
					if (this.rowState.selected[i]) {
						datas.splice(i, 1)
						this.rowState.selected.splice(i, 1)
					}
				}
				this.data.data = datas
				msg.info('del2')
			}).always(() => {
				progress.hide()
			})

			return promise
		}
	}
}

export default class DataSearch {
	name = 'DataSearch'
	constructor(meta) {
		this.meta = meta
		Object.assign(this, DataSearchProtocol)
		this.init()
	}
}