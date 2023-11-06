<template>
    <div class="container">
        <div class="info">
            <div class="timer">
                <p>{{formattedElapsedTime}}</p>
            </div>
            <div class="difficult">
                <form @submit.prevent>
                    <button class="level" @click="startGame(8)">8</button>
                    <button class="level" @click="startGame(12)">12</button>
                    <button class="level" @click="startGame(20)">20</button>
                </form>
            </div>
            <div class="leaderboard">
                <h3>Easy level top</h3>
                <table v-for:="leader in easyTop">
                    <tr v-for:="player in leader">
                        <td>
                            {{ player.name }} -
                        </td>
                        <td>
                            {{ player.time }}
                        </td>
                    </tr>
                </table>
                <h3>Medium level top</h3>
                <table v-for:="leader in mediumTop">
                    <tr v-for:="player in leader">
                        <td>
                            {{ player.name }} -
                        </td>
                        <td>
                            {{ player.time }}
                        </td>
                    </tr>
                </table>
                <h3>Hard level top</h3>
                <table v-for:="leader in hardTop">
                    <tr v-for:="player in leader">
                        <td>
                            {{ player.name }} -
                        </td>
                        <td>
                            {{ player.time }}
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div v-if="session === 0"></div>

        <div v-else>
            <div class="result" 
                v-if="gameStatus === 'LOOSE'">{{ stop() }}<strong>You loose</strong>
            </div>
                <div class="result" v-else-if="gameStatus === 'WIN'">
                    {{ stop() }}
                    <strong>You win!!!</strong>
                    <form @submit.prevent>
                        <label for="player"></label>
                        <input class="player" type="text" v-model="name">
                        <button @click="sendRecord">Send</button>
                    </form>
                </div>
            <div v-else>
                <div  class="field"  v-for:="(row, rowIndex) in answer" :key="rowIndex">
                    <span class="row" 
                        v-for:="(cell, cellIndex) in row"
                        :key="cellIndex"
                        @click="play(rowIndex, cellIndex)"
                        @contextmenu.prevent="handleContextMenu"
                        @click.right.prevent = "performRightClickAction(rowIndex, cellIndex)"
                        >
                        
                        <button class="fieldblock"
                            v-if="cell === null"
                            :style="{color: 'grey'}"
                            >X
                        </button>
                        <button class="fieldblock"
                            v-else-if="cell === '0'"
                            disabled
                            :style="{color: 'grey'}"
                            >0
                        </button>
                        <button class="fieldblock"
                            v-else-if="cell === 'disable'"
                            disabled
                            :style="{color: 'red'}"
                            >F
                        </button>
                        <button class="fieldblock"
                            v-else
                            >{{ cell }}
                        </button>

                    </span>
                </div>
            </div>
        </div>
         
    </div>   
</template>
<script>
import axios from "axios";

const baseURL = process.env.VUE_APP_API_URL;

export default {
    data() {
    return {
      name: null,
      answer: [null],
      gameStatus: '',
      easyTop: [],
      mediumTop: [],
      hardTop: [],
      coordinate: {
        x: null,
        y: null
      },
      session: 0,
      time: 0,
      timer: undefined,
      difficult: null
      
      
    };
  },

  mounted() {
    axios.get(baseURL + '/api/leaders').then(
        response => (
            this.easyTop.push(response.data[0]),
            this.mediumTop.push(response.data[1]),
            this.hardTop.push(response.data[2])
        )
    ).catch(error => {
        console.error("Ощибка подключения к бэкенду:", error);
    })
    
  },

  computed: {
    formattedElapsedTime() {
      const date = new Date(null);
      date.setSeconds(this.time / 1000);
      const utc = date.toUTCString();
      return utc.substr(utc.indexOf(":") - 1, 8);
    }
  },

  methods: {
    startGame(difficult) {
        this.difficult = difficult;
        axios.get(
            baseURL + '/api/start/' + difficult
        ).then(response => (
            this.session = response.data.sessionId,
            this.answer = response.data.field,
            this.gameStatus = response.data.status
        )).then(this.reset).then(this.start)
    },

    play(x, y) {
        axios.post(
            baseURL + '/api/play',
            [
                this.session, y, x
            ],  
        ).then(response => (
            this.gameStatus = response.data.status,
            this.answer = response.data.field
        ))
    },

    handleContextMenu(event) {
      event.preventDefault();
    },

    performRightClickAction(x, y) {
      axios.post(
        baseURL + '/api/flag',
        [
            this.session, y, x
        ], 
      ).then(response => (
        this.answer = response.data.field
      ))
    },

    sendRecord() {
        axios.post(baseURL + '/api/record', {
            time: this.time,    
            name: this.name,
            difficult: this.difficult
        }).then(this.name = "")
    },

    start() {
      this.timer = setInterval(() => {
        this.time += 1000;
      }, 1000);
    },

    stop() {
      clearInterval(this.timer);
    },

    reset() {
      this.time = 0;
    }
  }
}
</script>

<style scoped>

.container {
    display: flex;
}

.timer {
    font-size: 30px;
}

.difficult {
    width: 150px;
}

.level {
    width: 40px;
    height: 40px;
    font-size: 20px;
}
.field {
    width: 1000px;
}
.row {
    display: inline-flex;
    background-color: grey;
}

.fieldblock {
    height:20px;
    width:20px;
    background-color: grey;
    font-size: 10px;
    margin: 1px;
}

.result {
    size: 500;
}

</style>