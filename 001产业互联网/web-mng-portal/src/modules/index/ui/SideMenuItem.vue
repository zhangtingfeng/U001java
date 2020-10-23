<template>
	<li class="treeview" :data-id="menu.data.id">
		<a :href="isNode ? '#' : `#!menu/${menu.data.id}`">
			<!-- <i :class="'fa fa-' + menu.data.icon"></i> -->
			<img :src="`imgs/menus/${menu.data.icon}_grey.svg`" class="normal" />
			<img :src="`imgs/menus/${menu.data.icon}_green.svg`" class="hot" />
			<span>{{ $t(`menus.${menu.data.id}`) }}</span>
			<span class="pull-right-container" v-if="isNode">
				<i class="fa fa-angle-left pull-right"></i>
			</span>
		</a>
		<ul v-if="isNode" class="treeview-menu">
			<side-menu-item v-for="m in menu.children" v-bind:key="m.data.id" :menu="m"></side-menu-item>
		</ul>
	</li>
</template>

<script>
export default {
	name: "SideMenuItem",
	props: ["menu"],
	computed: {
		isNode() {
			return this.menu.children && this.menu.children.length > 0
		},
	},
}
</script>

<style lang="less" scoped>
li {
	a {
		font-size: 16px;
		margin: 0;
		padding: 16px auto;
		font-weight: 400 !important;
	}
	img {
		width: 16px;
		height: 16px;
	}
	img.hot {
		display: none;
	}

	&.active > a,
	a:hover {
		img.hot {
			display: inline;
		}
		img.normal {
			display: none;
		}

		font-weight: 400 !important;
	}
	.treeview-menu {
		padding-left: 20px;
	}
}
</style>
