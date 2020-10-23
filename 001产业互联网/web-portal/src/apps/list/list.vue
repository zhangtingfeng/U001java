<template>
  <div class="goods-list">
    <div class="goods-list-item" v-for="(item,idx) of lists" :key="idx">
      <goods :goods="item" />
    </div>
  </div>
</template>

<script>
import goods from "@/apps/template/goods";
export default {
  props: ["lists"],
  components: { goods },
  data() {
    return {};
  },
  watch: {},
};
</script>

<style lang="less" scoped>
@import url("@/apps/template/common.less");

.goods-list {
  display: flex;
  flex-flow: row wrap;

  .goods-list-item {
    flex: 0;
    padding: 10px;
    min-width: 20%;
  }
}
</style>