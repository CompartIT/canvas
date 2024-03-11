package com.epicor.canvas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.epicor.canvas.R

@Composable
fun HomePage(navController: NavHostController) {
    Column {
        TopLayout()
        ContentLayout()
        BottomLayout(navController)
    }
}
@Composable
fun PackageDialog(onDismiss: () -> Unit, function: () -> Unit) {

    Dialog(onDismissRequest = onDismiss, content = {
        BoxWithConstraints {
            val width = maxWidth.coerceAtMost(1046.dp)
            val height = maxHeight.coerceAtMost(666.dp)

            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .background(Color(0xffffffff), shape = RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // 在这里添加你的内容
                }
            }
        }
    })
}



@Composable
fun BottomLayout(navController: NavHostController) {

    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 64.dp, top = 53.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(294.dp)
                .height(397.dp)
                .background(Color(0xff00AEEF), shape = RoundedCornerShape(15.dp))
                .clickable { showDialog = true },
        ) {
            Column(
                modifier = Modifier
                    .padding(1.dp)
                    .width(294.dp)
                    .height(340.dp)
                    .background(Color.White, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.home_first),
                    contentDescription = null,
                    modifier = Modifier.size(
                        255.dp
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp),
                    text = "批号包装",
                    color = Color(0xff3d3d3d),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }

            if (showDialog){
                PackageDialog(onDismiss = {showDialog = false}) {

                }}
        }

        Box(
            modifier = Modifier
                .padding(start = 79.dp)
                .width(294.dp)
                .height(397.dp)

                .background(Color(0xffFBA98D), shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(1.dp)
                    .width(294.dp)
                    .height(340.dp)
                    .background(Color.White, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.home_two),
                    contentDescription = null,
                    modifier = Modifier
                        .size(
                            255.dp
                        )
                        .padding(start = 25.dp)
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp),
                    text = "包装追溯",
                    color = Color(0xff3d3d3d),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 79.dp)
                .width(294.dp)
                .height(397.dp)

                .background(Color(0xff439B38), shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(1.dp)
                    .width(294.dp)
                    .height(340.dp)
                    .background(Color.White, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.home_third),
                    contentDescription = null,
                    modifier = Modifier.size(
                        255.dp
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp),
                    text = "包装合并",
                    color = Color(0xff3d3d3d),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }


        Box(
            modifier = Modifier
                .padding(start = 79.dp)
                .width(294.dp)
                .height(397.dp)

                .background(Color(0xffCF2637), shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(1.dp)
                    .width(294.dp)
                    .height(340.dp)
                    .background(Color.White, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.home_fourth),
                    contentDescription = null,
                    modifier = Modifier.size(
                        255.dp
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp),
                    text = "包装撤销",
                    color = Color(0xff3d3d3d),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
        }

    }
}

@Composable
fun ContentLayout() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(359.dp)
            .padding(start = 64.dp, top = 31.dp, end = 46.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xff00aeef), Color(0xffd8d8d8)),
                    startY = 0.0f,
                    endY = Float.POSITIVE_INFINITY
                ), shape = RoundedCornerShape(15.dp)
            )
    ) {
        // Compart Logo
        Image(
            painter = painterResource(id = R.mipmap.compart_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 61.dp, top = 54.dp)
                .size(width = 198.dp, height = 50.dp)
        )

        // right image
        Image(
            painter = painterResource(id = R.mipmap.home_right_logo),
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.TopEnd
                )
                .padding(top = 5.dp, end = 5.dp)
                .size(width = 250.dp, height = 230.dp)
        )

        // Title
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 90.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "包装执行系统",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Packaging System",
                color = Color(0xff4e4e4e),
                fontSize = 30.sp,
                fontWeight = FontWeight.Thin
            )
        }

        // 肯发制造
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 61.dp)
        ) {
            Text(
                text = "肯发制造 4.0",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Perfect Production",
                color = Color(0xff4e4e4e),
                fontSize = 30.sp,
                fontWeight = FontWeight.Thin
            )
        }

    }
}

@Composable
fun TopLayout() {
    Row(
        modifier = Modifier
            .height(84.dp)
            .padding(start = 64.dp, top = 24.dp, end = 46.dp)
            .background(Color.White, shape = RoundedCornerShape(15.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.mipmap.compart_logo),
            contentDescription = null,
            modifier = Modifier
                .width(198.dp)
                .height(50.dp)
                .padding(start = 26.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "工号:53881", color = Color(0xff6e6b7b), fontSize = 24.sp)

        Image(
            painter = painterResource(id = R.mipmap.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(65.dp)
                .padding(start = 26.dp)
        )

        Image(
            painter = painterResource(id = R.mipmap.menu),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 26.dp, end = 44.8.dp)
                .size(45.dp)
        )
    }
}