import "@f/vendor"
import Vue from 'vue'
import Order from "./app"
import { i18n } from '@f/framework'
import user from "@/apps/user";

user.init()
new Vue({
	el: '#app',
	i18n,
	template: '<Order/>',
	components: {
		Order
	}
})