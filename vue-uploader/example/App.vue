<template>
  <uploader :options="options" :file-status-text="statusText" class="uploader-example" ref="uploader" @file-complete="fileComplete" @complete="complete"></uploader>
</template>

<script>
  import axios from 'axios'
  export default {
    data () {
      return {
        options: {
          target: '/booter/u/upload', // '//jsonplaceholder.typicode.com/posts/',
          testChunks: false,
          simultaneousUploads: 5, //并发数
          chunkSize: 8 * 1024 * 1024 //块大小
        },
        attrs: {
          accept: '*/*'
        },
        statusText: {
          success: '成功了',
          error: '出错了',
          uploading: '上传中',
          paused: '暂停中',
          waiting: '等待中'
        }
      }
    },
    methods: {
      complete () {
        console.log('complete', arguments)
      },
      fileComplete () {
        console.log('file complete', arguments)
        const file = arguments[0].file
        let url = '/booter/u/merge?filename=' + file.name + '&guid=' + arguments[0].uniqueIdentifier
        axios.get(url).then(function (response) {
          console.log(response)
        }).catch(function (error) {
          console.log(error)
        })
      }
    },
    mounted () {
      this.$nextTick(() => {
        window.uploader = this.$refs.uploader.uploader
      })
    }
  }
</script>

<style>
  .uploader-example {
    width: 880px;
    padding: 15px;
    margin: 40px auto 0;
    font-size: 12px;
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
  }
  .uploader-example .uploader-btn {
    margin-right: 4px;
  }
  .uploader-example .uploader-list {
    max-height: 440px;
    overflow: auto;
    overflow-x: hidden;
    overflow-y: auto;
  }
</style>
