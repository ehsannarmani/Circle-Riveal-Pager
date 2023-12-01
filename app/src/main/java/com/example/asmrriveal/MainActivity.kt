package com.example.asmrriveal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asmrriveal.ui.theme.AsmrRivealTheme
import kotlin.math.absoluteValue
import kotlin.math.sqrt

data class Location(
    val image:Int,
    val title:String,
    val description:String
)
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page:Int) = (currentPage-page)+currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int) = offsetForPage(page).coerceAtMost(0f)

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int) = offsetForPage(page).coerceAtLeast(0f)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsmrRivealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val locations = listOf(
                        Location(
                            image = R.drawable.tabriz,
                            title = "Tabriz",
                            description = "Tabriz is the capital city of East Azerbaijan Province, in northwestern Iran. Tabriz Bazaar, once a major Silk Road market, is a sprawling brick-vaulted complex selling carpets, spices and jewelry"
                        ),
                        Location(
                            image = R.drawable.dubai,
                            title = "Dubai",
                            description = "Dubai is a city and emirate in the United Arab Emirates known for luxury shopping, ultramodern architecture and a lively nightlife scene. Burj Khalifa, an 830m-tall tower, dominates the skyscraper-filled skyline."
                        ),
                        Location(
                            image = R.drawable.los_angeles,
                            title = "Los Angeles",
                            description = "Los Angeles is a sprawling Southern California city and the center of the nation’s film and television industry. Near its iconic Hollywood sign, studios such as Paramount Pictures, Universal and Warner Brothers offer behind-the-scenes tours."
                        ),
                        Location(
                            image = R.drawable.london,
                            title = "London",
                            description = "London, the capital of England and the United Kingdom, is a 21st-century city with history stretching back to Roman times."
                        ),
                        Location(
                            image = R.drawable.sweden,
                            title = "Sweden",
                            description = "Sweden is a Scandinavian nation with thousands of coastal islands and inland lakes, along with vast boreal forests and glaciated mountains."
                        ),
                        Location(
                            image = R.drawable.kazan,
                            title = "Kazan",
                            description = "Kazan is a city in southwest Russia, on the banks of the Volga and Kazanka rivers. The capital of the Republic of Tatarstan"
                        ),
                    )
                    val state = rememberPagerState()

                    val (offsetY,setOffsetY) = remember {
                        mutableStateOf(0f)
                    }
                    HorizontalPager(
                        state = state,
                        pageCount = locations.count(),
                        modifier= Modifier
                            .pointerInteropFilter {
                                setOffsetY(it.y)
                                false
                            }
                            .padding(16.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {page->
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset = state.offsetForPage(page)
                                translationX = size.width * pageOffset

                                val endOffset = state.endOffsetForPage(page)
                                shadowElevation = 20f
                                shape = CirclePath(
                                    progress = 1f - endOffset.absoluteValue,
                                    origin = Offset(
                                        size.width,
                                        offsetY
                                    )
                                )
                                clip = true

                                val abslouteOffset = state.offsetForPage(page).absoluteValue
                                val scale = 1f + (abslouteOffset.absoluteValue * .3f)

                                scaleX = scale
                                scaleY = scale

                                val startOffset = state.startOffsetForPage(page)
                                alpha = (2f - startOffset) / 2
                            }){
                            val location = locations[page]
                            Image(
                                painter = painterResource(id = location.image),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier=Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter)
                                    .fillMaxHeight(.5f)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = .7f)
                                            )
                                        )
                                    )
                            )
                            Column(modifier= Modifier
                                .align(Alignment.BottomCenter)
                                .padding(18.dp)) {
                                Text(text = location.title, fontSize = 26.sp, fontWeight = FontWeight.Bold,color = Color.White)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = location.description, fontSize = 16.sp,color = Color(0xffcccccc))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun s() {
    
}
class CirclePath(private val progress:Float,private val origin:Offset = Offset(0f,0f)):Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val center = Offset(
            x = size.center.x - ((size.center.x-(origin.x))*(1f-progress)),
            y= size.center.y - ((size.center.y)-origin.y)*(1f-progress)
        )
        val radius = (sqrt(
            (size.height*size.height)+(size.width*size.width)
        )*.5f)*progress
        return Outline.Generic(
            Path().apply {
                addOval(Rect(center = center,radius = radius))
            }
        )
    }

}
