<template>
	<div class="main-catalog">
		<!-- <div class="main-catalog" @mouseover="showPopup(true)" @mouseout="showPopup(false)"> -->
		<div class="catalog-list">
			<div class="header"><i class="fa fa-th"></i> 全部商品分类</div>

			<template v-for="item of lists">
				<a class="header1" href="#" @click.prevent="" :key="item.id">
					<img class="img1" src="imgs/main/icon_apple_white.png" />
					<img class="img2" src="imgs/main/icon_apple_green.png" />
					<span>{{ item.name }}</span>
				</a>

				<a
					:href="`#!AppList?c=${c.id}`"
					class="item"
					v-for="c of item.items"
					:key="c.id"
					:class="activeItem === c.id ? 'active' : ''"
					@mouseover="activeItem = item.id"
				>
					<img class="img1" src="imgs/main/icon_apple_white.png" />
					<img class="img2" src="imgs/main/icon_apple_green.png" />
					<span>{{ c.name }}</span>
				</a>
			</template>
		</div>
		<!-- <div class="popup" :style="`display: ${popup ? 'block' : 'none'};`">
			<div class="bg" />
			<filter-box class="filter-box" :catalog="activeItem" />
		</div> -->
	</div>
</template>

<script>
// import FilterBox from "./filter"
import CatalogData from "@/apps/catalog"
let POPUP_HANDLER = 0

export default {
	// components: { FilterBox },
	data() {
		return {
			// popup: false,
			activeItem: "",
			lists: [{ id: "0", name: "全部" }],
		}
	},
	mounted() {
		this.load()
	},
	methods: {
		async load() {
			this.lists = await CatalogData.loadCatalogTree()
		},
		// showPopup(val) {
		// 	if (POPUP_HANDLER) clearTimeout(POPUP_HANDLER)
		// 	POPUP_HANDLER = setTimeout(() => {
		// 		this.popup = val
		// 	}, 100)
		// },
	},
}
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.main-catalog {
	.catalog-list {
		border-left: 1px solid #ccc;
		border-right: 1px solid #ccc;
		.header {
			border-left: 2px solid @color1;
			padding: 10px 15px;
			background-color: @bg3;
			i {
				color: @color1;
			}
		}

		a {
			display: block;
			line-height: 24px;
			padding-left: 10px;
			border-bottom: 1px solid #ccc;
			img {
				width: 20px;
				height: 20px;
			}
		}

		.header1,
		.item:hover,
		.item.active {
			color: @text2;
			background-color: @color1;
			border-color: @color1;

			.img1 {
				display: inline;
			}
			.img2 {
				display: none;
			}
		}

		.item {
			color: @text1;
			padding-left: 30px;
			.img1 {
				display: none;
			}
			.img2 {
				display: inline;
			}
		}
	}

	// .popup {
	// 	position: absolute;
	// 	z-index: 100;
	// 	left: 195px;
	// 	top: 38px;
	// 	width: 800px;
	// 	height: 378px;

	// 	.bg {
	// 		position: absolute;
	// 		left: 0;
	// 		top: 0;
	// 		right: 0;
	// 		bottom: 0;
	// 		background-color: @text3;
	// 		z-index: 1;
	// 		opacity: 0.5;
	// 	}

	// 	.filter-box {
	// 		position: absolute;
	// 		left: 0;
	// 		top: 0;
	// 		z-index: 100;
	// 	}
	// }
}
</style>
