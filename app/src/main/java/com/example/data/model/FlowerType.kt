package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * 초등학교 5~6학년 과학 교과 과정(식물의 한살이, 식물의 구조와 기능, 생물과 환경)과
 * 직접 연계된 대표 식물/꽃 모델입니다.
 */
enum class FlowerType(
    val koreanName: String,
    val englishName: String,
    val scienceCurriculum: String,
    val scienceDetail: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val centerColor: Color,
    val petalCount: Int,
    val plantCategory: String
) {
    KIDNEY_BEAN(
        koreanName = "강낭콩",
        englishName = "Kidney Bean",
        scienceCurriculum = "초등 5학년 1학기: 식물의 한살이",
        scienceDetail = "씨앗에서 싹이 트고 떡잎 2장이 나온 뒤 본잎이 자라며 꼬투리 열매를 맺어요.",
        primaryColor = Color(0xFF66BB6A),
        secondaryColor = Color(0xFF388E3C),
        centerColor = Color(0xFF8D6E63),
        petalCount = 5,
        plantCategory = "씨앗과 발아 대표 식물"
    ),
    BALSAM(
        koreanName = "봉선화",
        englishName = "Balsam",
        scienceCurriculum = "초등 5~6학년: 줄기의 물관 관찰",
        scienceDetail = "붉은 잉크 물을 빨아들여 줄기의 물관을 관찰하는 대표적인 과학 실험 식물이에요.",
        primaryColor = Color(0xFFE91E63),
        secondaryColor = Color(0xFFC2185B),
        centerColor = Color(0xFFFFEB3B),
        petalCount = 5,
        plantCategory = "줄기와 물관 실험 식물"
    ),
    SUNFLOWER(
        koreanName = "해바라기",
        englishName = "Sunflower",
        scienceCurriculum = "초등 5학년 2학기: 빛과 식물의 성장",
        scienceDetail = "햇빛을 향해 큰 꽃을 피우고 잎에서 활발한 광합성을 통해 씨앗을 가득 맺어요.",
        primaryColor = Color(0xFFFFC107),
        secondaryColor = Color(0xFFFFA000),
        centerColor = Color(0xFF5D4037),
        petalCount = 12,
        plantCategory = "광합성과 양지 식물"
    ),
    MORNING_GLORY(
        koreanName = "나팔꽃",
        englishName = "Morning Glory",
        scienceCurriculum = "초등 5학년: 덩굴줄기와 통꽃",
        scienceDetail = "줄기가 지지대를 감고 올라가며, 꽃잎이 하나로 붙어 있는 아름다운 통꽃이에요.",
        primaryColor = Color(0xFF5C6BC0),
        secondaryColor = Color(0xFF3949AB),
        centerColor = Color(0xFFFFEB3B),
        petalCount = 5,
        plantCategory = "덩굴식물 & 통꽃 구조"
    ),
    DANDELION(
        koreanName = "민들레",
        englishName = "Dandelion",
        scienceCurriculum = "초등 5~6학년: 씨앗의 번식과 적응",
        scienceDetail = "꽃이 진 자리에 하얀 갓털(낙하산) 씨앗이 맺혀 바람을 타고 멀리 퍼져나가요.",
        primaryColor = Color(0xFFFFEB3B),
        secondaryColor = Color(0xFFFBC02D),
        centerColor = Color(0xFFFFF9C4),
        petalCount = 14,
        plantCategory = "바람에 날리는 씨앗"
    ),
    LILY(
        koreanName = "백합",
        englishName = "Lily",
        scienceCurriculum = "초등 6학년 1학기: 꽃의 완벽한 구조",
        scienceDetail = "암술, 수술, 꽃잎, 꽃받침의 4요소를 모두 갖춘 대표적인 '완전화' 표본이에요.",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFFE8F5E9),
        centerColor = Color(0xFFFFA000),
        petalCount = 6,
        plantCategory = "꽃의 구조 완전화"
    ),
    TOMATO(
        koreanName = "방울토마토",
        englishName = "Cherry Tomato",
        scienceCurriculum = "초등 5학년 1학기: 꽃과 열매의 발달",
        scienceDetail = "노란 꽃이 피고 수분된 후 씨방이 자라 탐스러운 열매(토마토)가 맺혀요.",
        primaryColor = Color(0xFFE53935),
        secondaryColor = Color(0xFFC62828),
        centerColor = Color(0xFFFFEE58),
        petalCount = 5,
        plantCategory = "열매 맺기 식물"
    ),
    HIBISCUS(
        koreanName = "무궁화",
        englishName = "Rose of Sharon",
        scienceCurriculum = "초등 6학년: 꽃의 수분과 생식",
        scienceDetail = "우리나라 국화로, 길게 뻗은 암술머리와 수많은 수술의 생식 구조를 잘 볼 수 있어요.",
        primaryColor = Color(0xFFF48FB1),
        secondaryColor = Color(0xFFAD1457),
        centerColor = Color(0xFFC2185B),
        petalCount = 5,
        plantCategory = "우리나라 꽃 & 꽃의 수분"
    ),
    LOTUS(
        koreanName = "연꽃",
        englishName = "Lotus",
        scienceCurriculum = "초등 5학년 2학기: 물에 사는 식물",
        scienceDetail = "물속 진흙에 뿌리를 두고, 잎과 줄기 속 공기구멍(통기조직)으로 숨 쉬는 수생식물이에요.",
        primaryColor = Color(0xFFF06292),
        secondaryColor = Color(0xFFE91E63),
        centerColor = Color(0xFFFFD54F),
        petalCount = 10,
        plantCategory = "물속 수생식물 적응"
    ),
    CACTUS(
        koreanName = "선인장",
        englishName = "Cactus",
        scienceCurriculum = "초등 5학년 2학기: 사막 환경 적응",
        scienceDetail = "건조한 환경에서 수분을 빼앗기지 않기 위해 잎이 가시로 변하고 줄기에 물을 저장해요.",
        primaryColor = Color(0xFFFF7043),
        secondaryColor = Color(0xFFF4511E),
        centerColor = Color(0xFFFFD54F),
        petalCount = 8,
        plantCategory = "건조 환경 적응 식물"
    ),
    // 하위 호환성을 위한 별칭
    TULIP(
        koreanName = "튤립",
        englishName = "Tulip",
        scienceCurriculum = "초등 5학년: 알뿌리(비늘줄기) 식물",
        scienceDetail = "흙 속의 알뿌리(비늘줄기)에서 영양분을 저장했다가 봄에 싹을 틔워요.",
        primaryColor = Color(0xFFE91E63),
        secondaryColor = Color(0xFFC2185B),
        centerColor = Color(0xFFFFEB3B),
        petalCount = 6,
        plantCategory = "알뿌리 번식 식물"
    ),
    ROSE(
        koreanName = "장미",
        englishName = "Rose",
        scienceCurriculum = "초등 5학년: 식물의 줄기 보호",
        scienceDetail = "초식 동물로부터 자신을 지키기 위해 줄기에 단단한 가시를 발달시켰어요.",
        primaryColor = Color(0xFFE53935),
        secondaryColor = Color(0xFFB71C1C),
        centerColor = Color(0xFFFFCDD2),
        petalCount = 8,
        plantCategory = "줄기 적응 식물"
    ),
    DAISY(
        koreanName = "데이지",
        englishName = "Daisy",
        scienceCurriculum = "초등 5~6학년: 국화과 두상화",
        scienceDetail = "작은 꽃 수십 개가 모여 하나의 큰 꽃처럼 보이는 두상화 구조예요.",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFFF5F5F5),
        centerColor = Color(0xFFFFD54F),
        petalCount = 12,
        plantCategory = "두상화 복합꽃"
    ),
    CHERRY_BLOSSOM(
        koreanName = "벚꽃",
        englishName = "Cherry Blossom",
        scienceCurriculum = "초등 5학년: 봄에 피는 나무꽃",
        scienceDetail = "잎이 나오기 전에 먼저 꽃이 피어 곤충을 유인하고 수분을 진행해요.",
        primaryColor = Color(0xFFFF80AB),
        secondaryColor = Color(0xFFFF4081),
        centerColor = Color(0xFFFFE57F),
        petalCount = 5,
        plantCategory = "봄꽃 나무의 한살이"
    ),
    LAVENDER(
        koreanName = "라벤더",
        englishName = "Lavender",
        scienceCurriculum = "초등 5~6학년: 향기를 내는 식물",
        scienceDetail = "잎과 꽃의 기름샘에서 독특한 향기를 풍겨 곤충을 부르고 해충을 쫓아요.",
        primaryColor = Color(0xFFAB47BC),
        secondaryColor = Color(0xFF8E24AA),
        centerColor = Color(0xFFE1BEE7),
        petalCount = 6,
        plantCategory = "기름샘과 향기 식물"
    ),
    MARIGOLD(
        koreanName = "금잔화(매리골드)",
        englishName = "Marigold",
        scienceCurriculum = "초등 5학년: 한해살이 화초",
        scienceDetail = "꽃 색이 짙고 선명하여 꿀벌과 나비가 꿀을 찾아오기 쉬워요.",
        primaryColor = Color(0xFFFF9800),
        secondaryColor = Color(0xFFF57C00),
        centerColor = Color(0xFF795548),
        petalCount = 10,
        plantCategory = "곤충을 유인하는 꽃"
    ),
    BLUEBELL(
        koreanName = "초롱꽃",
        englishName = "Bellflower",
        scienceCurriculum = "초등 5~6학년: 종 모양 통꽃",
        scienceDetail = "아래를 향해 종처럼 피어 빗물이 꽃가루에 닿는 것을 막아줘요.",
        primaryColor = Color(0xFF42A5F5),
        secondaryColor = Color(0xFF1E88E5),
        centerColor = Color(0xFFFFF9C4),
        petalCount = 5,
        plantCategory = "꽃가루 보호 통꽃"
    ),
    HYDRANGEA(
        koreanName = "수국",
        englishName = "Hydrangea",
        scienceCurriculum = "초등 5~6학년: 토양 산도와 꽃 색",
        scienceDetail = "토양의 성질(산성, 알칼리성)에 따라 꽃 색깔이 파란색, 분홍색으로 달라져요.",
        primaryColor = Color(0xFF7986CB),
        secondaryColor = Color(0xFF5C6BC0),
        centerColor = Color(0xFFE8EAF6),
        petalCount = 4,
        plantCategory = "토양 반응 식물"
    )
}
