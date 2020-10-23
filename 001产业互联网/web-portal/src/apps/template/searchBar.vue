<template>
	<nav class="navbar navbar-default">
		<div class="container">
			<div class="row">
				<div class="col-md-2">
					<div class="navbar-header">
						<img src="imgs/logo.svg" width="188px" height="85px" alt="汇农苹果网" />
					</div>
				</div>
				<div class="col-md-6 col-md-offset-1">
					<form class="search-form" action @submit.prevent="search">
						<div class="input-group input-group-lg input-search">
							<input type="text" class="form-control" v-model="val" placeholder="根据卖家信息或者发布标题检索" />
							<span class="input-group-btn">
								<button class="btn btn-primary" type="submit">搜&nbsp;索</button>
							</span>
						</div>
					</form>
				</div>
			</div>
		</div>
	</nav>
</template>

<script>
export default {
	props: ["args"],
	data() {
		return { val: this.args && this.args.q ? this.args.q : "" }
	},
	methods: {
		search() {
			let url = "index.html#!AppList?"
			if (this.args && this.args.c) {
				url += `c=${this.args.c}&`
			}
			if (this.args && this.args.p) {
				url += `p=${this.args.p}&`
			}
			url += `q=${encodeURIComponent(this.val)}`
			window.location = url
		},
	},
}
</script>
<style lang="less" scoped>
@import url("./common.less");

.navbar {
	background-color: @bg2;
	margin-bottom: 0;
	border: none;

	.search-form {
		margin: 20px auto;
		.input-search {
			input,
			button {
				border-color: @color1;
				border-radius: 0;
				border-color: @color1;
			}
			button {
				background-color: @color1;
			}
		}
	}
}
</style>
