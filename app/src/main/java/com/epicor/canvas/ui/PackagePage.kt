package com.epicor.canvas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.epicor.canvas.R

@Composable
fun PackagePage(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        TopLayout()
        PackageContent()
        PackageBottom()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageBottom() {

    Box(
        modifier = Modifier
            .padding(start = 61.dp, end = 46.dp, top = 36.dp)
            .fillMaxWidth()
            .height(616.dp)
            .background(Color(0xFFDFDFDF), shape = RoundedCornerShape(15.dp))
    ) {

        Box(
            modifier = Modifier
                .height(589.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(15.dp))
        ) {
            Text(
                text = "包装类型",
                Modifier.padding(start = 40.dp, top = 60.dp),
                fontSize = 14.sp,
                color = Color(0xFF3D3D3D)
            )
            var text by remember { mutableStateOf("") }

            Column(
               modifier = Modifier.padding(start = 40.dp, top = 90.dp)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                    singleLine = false, // 如果需要单行显示
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier
                                .width(200.dp)
                                .height(38.dp)
                                .clip(RoundedCornerShape(15.dp)) // 设置内边裁剪为圆角
                                .background(Color.White) // 修改背景色（这里改为白色以显示效果）
                                .border(
                                    1.dp,
                                    Color(0xFFDEDEDE),
                                    shape = RoundedCornerShape(15.dp)
                                ) // 保留原来的边框设置
                                .align(Alignment.CenterHorizontally),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally) // 确保整个输入框在父容器中水平居中
                )
            }
        }

    }

}

@Composable
fun PackageContent() {
    Row(
        modifier = Modifier
            .padding(start = 64.dp, top = 31.dp, end = 46.dp)
            .height(171.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xff00aeef), Color(0xffd8d8d8)),
                    startY = 0.0f,
                    endY = Float.POSITIVE_INFINITY
                ), shape = RoundedCornerShape(15.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // left Image
        Image(painter = painterResource(id = R.mipmap.package_box_image), contentDescription = null)

        // 肯发制造
        Column(
            modifier = Modifier
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

        Spacer(modifier = Modifier.weight(1f))


        // Title
        Column(
            modifier = Modifier
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

    }
}
