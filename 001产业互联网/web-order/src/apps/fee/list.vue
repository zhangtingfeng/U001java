<template>
	<table1 name="order/fee-process" :extend="ListData"></table1>
</template>

<script>
import { Table1 } from "@f/framework"

let ListData = {
	searchActions: [
		{ name: "query", clz: "primary", icon: "search" },
		{ name: "exp", icon: "download" },
	],
	listActions: [{ name: "process", icon: "edit" }],

	listProcess() {
		let row = this.$data.data.data[this.rowState.active]
		window.location = `#!m/order/flow-mng/nodeViewer?id=${row.processId}`
	},
}
export default {
	components: { Table1 },
	data() {
		return { ListData }
	},
}
</script>

<i18n-yaml>
cn:
  process:"操作"
en:
  process:"Process"
</i18n-yaml>
