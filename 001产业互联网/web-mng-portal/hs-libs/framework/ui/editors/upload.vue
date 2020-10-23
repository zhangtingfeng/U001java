<template>
    <file-upload :name="name" :validate="validate" :readonly="readonly" v-model="data" :url="action" :fileExt="fileExt" />
</template>

<script>
import FileUpload from "@c/ui/FileUpload";
const FILE_TYPES = { img: ["gif", "png", "jpg", "jpeg"], video: ["mp4"], excel:["xls", "xlsx"] };

export default {
  components: {
    FileUpload
  },
  data() {
    let ft = /[^-]*$/.exec(this.args)[0];
    return {
      data: this.value,
      action: `api/upload/${this.args}.json`,
      fileExt: FILE_TYPES[ft]
    };
  },
  props: {
    value: { default: "" },
    args: { default: "" },
    name: "",
    readonly: { default: false },
    validate: ""
  },
  watch: {
    data(val) {
      this.$emit("input", val);
    }
  }
};
</script>
