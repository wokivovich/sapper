<template>
    <div>
        <div class="timer">
            <p>{{formattedElapsedTime}}</p>
        </div>
        <div class="difficult">
            <form @submit.prevent>
                <button class="level" @click="startGame(8)">Easy</button>
                <button class="level" @click="startGame(12)">Medium</button>
                <button class="level" @click="startGame(20)">Hard</button>
            </form>
        </div>
        <div v-if="session === 0"></div>
        <div class="game" v-else>
            <div class="result" v-if="gameStatus === 'LOOSE'">{{ stop() }}<strong>You loose</strong></div>
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
                <form @submit.prevent>
                    <div class="line" v-for:="(line, index1) in answer">
                        <div>
                            <button 
                            class="fieldBlock" 
                            v-for:="(block, index2) in line"
                            @click="play(index1, index2)">
                                <label class="blk" v-if="block === null">X</label>
                                <label class="blk" v-else-if="block === '0'"></label>
                                <label class="blk" v-else>{{ block }}</label>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div> 
        <div class="leaderBoard">
            <h3>Top players</h3>
            <table v-for:="leader in leaders">
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
</template>
<script>
import axios from "axios";

export default {
    data() {
    return {
      name: null,
      answer: [null],
      gameStatus: '',
      leaders: [],
      coordinate: {
        x: null,
        y: null
      },
      session: 0,
      time: 0,
            timer: undefined
      
    };
  },

  mounted() {
    axios.get('http://localhost:5000/api/leaders').then(
        response => (
            this.leaders.push(response.data)
        )
    )
    
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
        axios.get(
            'http://localhost:5000/api/start/' + difficult
        ).then(response => (
            this.session = response.data.sessionId,
            this.answer = response.data.field,
            this.gameStatus = response.data.status
        )).then(this.reset).then(this.start)
    },

    play(x, y) {
        axios.post(
            'http://localhost:5000/api/play',
            [
                this.session, y, x
            ],  
        ).then(response => (
            this.gameStatus = response.data.status,
            this.answer = response.data.field
        ))
    },

    sendRecord() {
        axios.post('http://localhost:5000/api/record', {
            time: this.time,    
            name: this.name,
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
.difficult {
}
.line {
    margin: auto;
    width: 10000px;
}
.fieldBlock {
    height:100px;
    width:100px;
    background-color: grey;
}

.blk {
    margin-left: 1px;
    font-size: 50px;
}

.level {
    font-size: 100px;
    margin-left: 10px;
}

.result {
    size: 500;
}

.timer {
    font-size: 50px;
    float: left;
}

.leaderboard {
}
</style>