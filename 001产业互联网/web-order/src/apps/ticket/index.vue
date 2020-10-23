<template>
  <div>
    <div class="box-header box-solid bg-gray">
      <h3 class="box-title">发票审批流程</h3>
    </div>
    <div class="box-body">{{process.serviceId}}</div>
  </div>
</template>

<script>
export default {
  props: ["process"],
};
</script>
