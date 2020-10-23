<template>
	<div class="portal">
		<app-header />
		<app-search :args="args" />
		<app-main />
		<app-info />
		<app-footer />
	</div>
</template>

<script>
import AppHeader from "../template/header"
import AppFooter from "../template/footer"
import AppSearch from "../template/searchBar"
import AppInfo from "./info"
import AppMain from "./main"

export default {
	components: { AppHeader, AppSearch, AppMain, AppInfo, AppFooter },
	props: ["args"],
	data() {
		return {}
	},
}
</script>
