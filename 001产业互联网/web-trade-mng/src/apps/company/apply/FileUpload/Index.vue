<template>
  <div class="input-group">
    <input type="text" class="form-control" readonly :name="name" v-model="text" v-validate="validate">
    <input type="file" style="display:none;" @change="upload1" name="file">
    <div class="input-group-btn">
      <a class="btn btn-default" v-if="!!this.value" @click.prevent="view"><i class="fa fa-eye"></i></a>
      <a class="btn btn-default" v-if="!!this.value" @click.prevent="remove"><i class="fa fa-times"></i></a>
      <a class="btn btn-default" @click.prevent="upload0"><i class="fa fa-upload"></i></a>
    </div>
  </div>
</template>

<script>
import "./jquery.ajaxfileupload";

import { progress, msg, ajax } from "@f/vendor";

export default {
  data() {
    return {
      data: this.value,
      id: ""
    };
  },
  props: {
    value: { default: "" },
    name: { default: "" },
    readonly: { default: false },
    validate: { default: "" },
    url: { detaul: "" },
    params: {
      default: () => {
        return {};
      }
    },
    fileExt: {}
  },
  computed: {
    text() {
      return this.data || "请上传文件";
    }
  },
  watch: {
    value(val) {
      this.data = val;
    },
    data(val) {
      this.$emit("input", val);
    }
  },
  methods: {
    showError(txt) {
      if (txt) msg.info(txt, "error");
    },
    upload0() {
      this.$file.click();
    },
    upload1() { },
    upload2(d) {
      // error
      if (!d.status) {
        this.showError(d.message || "访问服务器错误");
      } else {
        this.data = d.name;
        this.id = d.id;
      }
    },
    view() {
      if (this.data) {
        window.open(`${this.url}/../${this.id}`);
      }
    },
    remove() {
      this.data = "";
    }
  },
  mounted() {
    this.$file = $(this.$el).find("input[type=file]");
    this.$file.ajaxfileupload({
      action: this.url,
      params: this.params,
      validate_extensions: !!this.fileExt,
      valid_extensions: this.fileExt,
      onStart: () => {
        progress.show();
      },
      onComplete: d => {
        this.$file.val("");
        progress.hide();
        this.upload2(d);
      }
    });
  }
};
</script>

<style scoped>
</style>