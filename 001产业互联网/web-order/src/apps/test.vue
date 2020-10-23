<template>
	<div>
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title">Expandable</h3>
				<div class="box-tools pull-right">
					<a href="#" class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
					</a>
				</div>
			</div>
			<div class="box-body">
				The body of the boxddddddddddddddddddddddddddddddd
			</div>
		</div>
	</div>
</template>

<script>
import { msg, ajax } from "@f/vendor"
//import { getUrlKey } from '@/utils';
export default {
	data() {
		return {
			user: {},
		}
	},
	methods: {
		async init() {
			try {
				let data = await ajax.get("api/session")
				if (data.user) {
					this.user = data.user
				}
			} catch {}
		},
	},
	mounted() {
		this.init()
	},
}
</script>

<style></style>
