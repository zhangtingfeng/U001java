<template>
	<div class="app-main">
		<div class="border-top"></div>
		<div class="container">
			<div class="row">
				<div class="col-md-2 left">
					<catalog />
				</div>
				<div class="col-md-10 right">
					<div class="tabs">
						<a v-for="t of tabs" :key="t.id" :class="t.clz" :href="t.href" target="_blank" >
							{{ t.name }}
							<img v-if="t.icon" :src="t.icon" />
						</a>
					</div>
					<div id="main-banner" class="carousel slide" data-ride="carousel">
						<ol class="carousel-indicators">
							<li data-target="#main-banner" data-slide-to="0" class="active"></li>
							<li data-target="#main-banner" data-slide-to="1"></li>
							<li data-target="#main-banner" data-slide-to="2"></li>
						</ol>
						<div class="carousel-inner">
							<div class="item" v-for="i of 3" :key="i" :class="i == 1 ? 'active' : ''">
								<img :src="`imgs/banner/${i}.jpg`" />
							</div>
						</div>
						<a class="left carousel-control" href="#main-banner" role="button" data-slide="prev">
							<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a>
						<a class="right carousel-control" href="#main-banner" role="button" data-slide="next">
							<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
							<span class="sr-only">Next</span>
						</a>
					</div>
				</div>
			</div>
		</div>
		<div class="border-bottom"></div>
	</div>
</template>

<script>
import catalog from "./catalog"

const TABS = [
	{ id: "1", name: "首页", clz: "active" },
	{ id: "2", name: "智慧供应链", icon: "imgs/tab_f.png", href:"http://www.huinong9.com/finance" },
	{ id: "3", name: "金融服务", icon: "imgs/tab_h.png", href:"http://www.huinong9.com/finance" },
	{ id: "4", name: "智慧种植", href:"http://www.huinong9.com/plant" },
	{ id: "5", name: "资讯中心", href:"http://www.huinong9.com/news" },
	{ id: "6", name: "汇农大学", href:"http://www.huinong9.com/university" },
	{ id: "7", name: "关于我们", href:"http://www.huinong9.com/aboutus" },
]

export default {
	components: { catalog },
	data() {
		return { tabs: TABS }
	},
}
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.app-main {
	background-color: @bg2;

	.left {
		padding-right: 0;
	}
	.right {
		padding-left: 0;

		.tabs a {
			line-height: 36px;
		}

		.carousel-inner {
			img {
				height: 351px;
				width: 100%;
			}
		}
	}
}
</style>
