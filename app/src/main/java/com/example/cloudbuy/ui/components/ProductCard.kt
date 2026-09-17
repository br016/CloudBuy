package com.example.cloudbuy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.ui.components.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val priceFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = modifier
            .width(170.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = CloudWhite)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) CloudRed else CloudLightText
                    )
                }
            }
            Column(Modifier.padding(12.dp)) {
                Text(
                    product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = CloudStar, modifier = Modifier.size(14.dp))
                    Text(" ${product.rating}", fontSize = 12.sp, color = CloudLightText)
                }
                Text(
                    priceFormat.format(product.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CloudBlueDark
                )
                product.originalPrice?.let {
                    Text(
                        priceFormat.format(it),
                        fontSize = 12.sp,
                        color = CloudLightText,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}