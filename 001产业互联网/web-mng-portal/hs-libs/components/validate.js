import Vue from "vue";
import {
  i18n
} from "./locale/index";
import VeeValidate from "vee-validate";

const config = {
  errorBagName: "errors",
  delay: 0,
  i18n,
  messages: null,
  strict: true
};
Vue.use(VeeValidate, config);