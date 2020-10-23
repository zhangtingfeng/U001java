import "@f/vendor"
import Vue from 'vue'
import Cart from "./app"
import { i18n } from '@f/framework'
import user from "@/apps/user";

user.init()

new Vue({
	el: '#app',
	i18n,
	template: '<cart/>',
	components: {
		Cart
	}
})