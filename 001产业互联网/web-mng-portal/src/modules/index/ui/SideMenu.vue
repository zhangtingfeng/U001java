<template>
	<aside class="main-sidebar">
		<section class="sidebar">
			<ul class="sidebar-menu tree" data-lte.tree data-follow-link="true">
				<side-menu-item v-for="m in menus" v-bind:key="m.data.id" :menu="m" />
			</ul>
		</section>
	</aside>
</template>

<script>
import menu from "../data/menu"
import SideMenuItem from "./SideMenuItem"

let ResizeMenuHandle

export default {
	data() {
		let menus = menu.tree()
		let msgs = menu.msgs()
		this.$i18n.mergeLocaleMessage("en", msgs.en)
		this.$i18n.mergeLocaleMessage("cn", msgs.cn)
		return { menus, data: menu.data }
	},
	methods: {
		updateMenus() {
			let $el = $(this.$el)
			let m = menu.get(this.data.curr)
			$el.find("li.active").removeClass("active")
			while (m) {
				$el.find(`li[data-id=${m.data.id}]`).addClass("menu-open active")
				m = m.parent
			}
		},
		resizeMenu() {
			// $("section.sidebar").css({ height: 0 });
			// if (ResizeMenuHandle) clearTimeout(ResizeMenuHandle)
			// ResizeMenuHandle = setTimeout(() => {
			//   let h = $("body").height();
			//   let h1 = $(".main-header").height();
			//   $("section.sidebar").css({ height: h - h1 });
			// }, 100);
		},
	},
	mounted() {
		$(this.$el)
			.find("ul.sidebar-menu.tree")
			.tree()
		this.updateMenus()
		$(window).resize(() => {
			this.resizeMenu()
		})
	},
	watch: {
		"data.curr"() {
			this.updateMenus()
		},
	},
	components: {
		SideMenuItem,
	},
}
</script>

<style lang="less" scoped>
// .sidebar-menu {
// 	// li > a {
// 	// 	font-size: 16px;
// 	// 	line-height: 32px;
// 	// }

// 	// > .sidebar > .sidebar-menu {
// 	// 	// > .treeview > a > i {
// 	// 	// 	font-size: 130%;
// 	// 	// }

// 	// 	// > .treeview > .treeview-menu {
// 	// 	// 	padding-left: 20px !important;
// 	// 	// }
// 	// }
// }
</style>
