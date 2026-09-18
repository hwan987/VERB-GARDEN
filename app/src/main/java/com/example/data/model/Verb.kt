package com.example.data.model

data class Verb(
    val id: String,
    val base: String,
    val past: String,
    val pastParticiple: String,
    val meaningKo: String,
    val flowerType: FlowerType,
    val distractorsPast: List<String>,
    val distractorsParticiple: List<String>
) {
    /**
     * Generates 3 choices for past tense quiz (1 correct + 2 distractors, randomized)
     */
    fun generatePastOptions(): List<String> {
        val options = mutableListOf(past)
        options.addAll(distractorsPast.shuffled().take(2))
        return options.distinct().shuffled().let {
            if (it.size < 3) {
                val fallback = listOf("${base}ed", "${base}d", "${base}t").filter { f -> f !in it }
                (it + fallback).take(3).shuffled()
            } else it
        }
    }

    /**
     * Generates 3 choices for past participle quiz (1 correct + 2 distractors, randomized)
     */
    fun generateParticipleOptions(): List<String> {
        val options = mutableListOf(pastParticiple)
        options.addAll(distractorsParticiple.shuffled().take(2))
        return options.distinct().shuffled().let {
            if (it.size < 3) {
                val fallback = listOf(past, "${base}en", "${base}ing").filter { f -> f !in it }
                (it + fallback).take(3).shuffled()
            } else it
        }
    }

    companion object {
        val DEFAULT_VERBS = listOf(
            Verb(
                id = "go",
                base = "go",
                past = "went",
                pastParticiple = "gone",
                meaningKo = "가다",
                flowerType = FlowerType.KIDNEY_BEAN, // 강낭콩 (초등 5학년: 씨앗의 발아)
                distractorsPast = listOf("goed", "gone", "goes"),
                distractorsParticiple = listOf("went", "goed", "going")
            ),
            Verb(
                id = "see",
                base = "see",
                past = "saw",
                pastParticiple = "seen",
                meaningKo = "보다",
                flowerType = FlowerType.BALSAM, // 봉선화 (초등 5~6학년: 줄기 물관 실험)
                distractorsPast = listOf("seed", "seen", "sawed"),
                distractorsParticiple = listOf("saw", "seed", "seeing")
            ),
            Verb(
                id = "eat",
                base = "eat",
                past = "ate",
                pastParticiple = "eaten",
                meaningKo = "먹다",
                flowerType = FlowerType.TOMATO, // 방울토마토 (초등 5학년: 꽃과 열매의 발달)
                distractorsPast = listOf("eated", "eaten", "eats"),
                distractorsParticiple = listOf("ate", "eated", "eating")
            ),
            Verb(
                id = "take",
                base = "take",
                past = "took",
                pastParticiple = "taken",
                meaningKo = "가져가다 / 타다",
                flowerType = FlowerType.SUNFLOWER, // 해바라기 (초등 5학년: 빛과 광합성)
                distractorsPast = listOf("taked", "taken", "tooked"),
                distractorsParticiple = listOf("took", "taked", "taking")
            ),
            Verb(
                id = "give",
                base = "give",
                past = "gave",
                pastParticiple = "given",
                meaningKo = "주다",
                flowerType = FlowerType.HIBISCUS, // 무궁화 (초등 6학년: 꽃의 구조와 수분)
                distractorsPast = listOf("gived", "given", "gaves"),
                distractorsParticiple = listOf("gave", "gived", "giving")
            ),
            Verb(
                id = "write",
                base = "write",
                past = "wrote",
                pastParticiple = "written",
                meaningKo = "쓰다",
                flowerType = FlowerType.DANDELION, // 민들레 (초등 5~6학년: 바람에 날리는 갓털 씨앗)
                distractorsPast = listOf("writed", "written", "wroted"),
                distractorsParticiple = listOf("wrote", "writen", "writed")
            ),
            Verb(
                id = "run",
                base = "run",
                past = "ran",
                pastParticiple = "run",
                meaningKo = "달리다",
                flowerType = FlowerType.MORNING_GLORY, // 나팔꽃 (초등 5학년: 덩굴줄기와 통꽃)
                distractorsPast = listOf("runned", "run", "rans"),
                distractorsParticiple = listOf("ran", "runned", "running")
            ),
            Verb(
                id = "sing",
                base = "sing",
                past = "sang",
                pastParticiple = "sung",
                meaningKo = "노래하다",
                flowerType = FlowerType.LILY, // 백합 (초등 6학년: 완전화의 표본)
                distractorsPast = listOf("singed", "sung", "song"),
                distractorsParticiple = listOf("sang", "singed", "singing")
            ),
            Verb(
                id = "swim",
                base = "swim",
                past = "swam",
                pastParticiple = "swum",
                meaningKo = "수영하다",
                flowerType = FlowerType.LOTUS, // 연꽃 (초등 5학년: 물에 사는 수생식물)
                distractorsPast = listOf("swimmed", "swum", "swamed"),
                distractorsParticiple = listOf("swam", "swimmed", "swimming")
            ),
            Verb(
                id = "make",
                base = "make",
                past = "made",
                pastParticiple = "made",
                meaningKo = "만들다",
                flowerType = FlowerType.CACTUS, // 선인장 (초등 5학년: 건조 환경 적응 식물)
                distractorsPast = listOf("maked", "maken", "mades"),
                distractorsParticiple = listOf("maked", "maken", "making")
            ),
            Verb(
                id = "come",
                base = "come",
                past = "came",
                pastParticiple = "come",
                meaningKo = "오다",
                flowerType = FlowerType.KIDNEY_BEAN,
                distractorsPast = listOf("comed", "came", "comes"),
                distractorsParticiple = listOf("came", "comed", "coming")
            ),
            Verb(
                id = "break",
                base = "break",
                past = "broke",
                pastParticiple = "broken",
                meaningKo = "부수다 / 깨다",
                flowerType = FlowerType.BALSAM,
                distractorsPast = listOf("breaked", "broken", "broked"),
                distractorsParticiple = listOf("broke", "breaked", "breaking")
            ),
            Verb(
                id = "buy",
                base = "buy",
                past = "bought",
                pastParticiple = "bought",
                meaningKo = "사다",
                flowerType = FlowerType.SUNFLOWER,
                distractorsPast = listOf("buyed", "boughten", "baught"),
                distractorsParticiple = listOf("buyed", "boughten", "buying")
            ),
            Verb(
                id = "sleep",
                base = "sleep",
                past = "slept",
                pastParticiple = "slept",
                meaningKo = "자다",
                flowerType = FlowerType.MORNING_GLORY,
                distractorsPast = listOf("sleeped", "slepted", "sleepen"),
                distractorsParticiple = listOf("sleeped", "sleepen", "sleeping")
            ),
            Verb(
                id = "drink",
                base = "drink",
                past = "drank",
                pastParticiple = "drunk",
                meaningKo = "마시다",
                flowerType = FlowerType.LOTUS,
                distractorsPast = listOf("drinked", "drunk", "dranken"),
                distractorsParticiple = listOf("drank", "drinked", "drinking")
            ),
            Verb(
                id = "grow",
                base = "grow",
                past = "grew",
                pastParticiple = "grown",
                meaningKo = "자라다 / 키우다",
                flowerType = FlowerType.KIDNEY_BEAN,
                distractorsPast = listOf("growed", "grown", "grewed"),
                distractorsParticiple = listOf("grew", "growed", "growing")
            ),
            Verb(
                id = "fly",
                base = "fly",
                past = "flew",
                pastParticiple = "flown",
                meaningKo = "날다",
                flowerType = FlowerType.DANDELION,
                distractorsPast = listOf("flied", "flown", "flewed"),
                distractorsParticiple = listOf("flew", "flied", "flying")
            ),
            Verb(
                id = "draw",
                base = "draw",
                past = "drew",
                pastParticiple = "drawn",
                meaningKo = "그리다",
                flowerType = FlowerType.HIBISCUS,
                distractorsPast = listOf("drawed", "drawn", "drewed"),
                distractorsParticiple = listOf("drew", "drawed", "drawing")
            ),
            Verb(
                id = "know",
                base = "know",
                past = "knew",
                pastParticiple = "known",
                meaningKo = "알다",
                flowerType = FlowerType.LILY,
                distractorsPast = listOf("knowed", "known", "knewed"),
                distractorsParticiple = listOf("knew", "knowed", "knowing")
            ),
            Verb(
                id = "speak",
                base = "speak",
                past = "spoke",
                pastParticiple = "spoken",
                meaningKo = "말하다",
                flowerType = FlowerType.TOMATO,
                distractorsPast = listOf("speaked", "spoken", "spoked"),
                distractorsParticiple = listOf("spoke", "speaked", "speaking")
            )
        )
    }
}
