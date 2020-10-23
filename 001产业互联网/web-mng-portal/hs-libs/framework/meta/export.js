let DataExportInternal = {
	init() {
		if (this.$form) return this

		this.$form = $('<form method="post"></form>').css({
			display: "none"
		}).attr({
			target: "_blank"
		})
		this.$data = $('<input type="hidden" name="data">')
		this.$form.append(this.$data)
		$("body").append(this.$form)
		return this
	},

	export(url, data) {
		this.$data.val(data)
		this.$form.attr({
			action: url
		}).submit()
	}
}


export default class DataExport {
	name = 'DataExport'
	constructor(meta) {
		this.meta = meta
		Object.assign(this, {
			searchExp() {
				for (let f in this.search) {
					if ("" == this.search[f]) this.search[f] = undefined
				}
				DataExportInternal.init().export(`${this.meta.url}/export.xlsx`, JSON.stringify(this.search))
			}
		})
	}
}