package com.kashsoft.insta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kashsoft.insta.Model.Comment
import com.kashsoft.insta.Model.User
import com.kashsoft.insta.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(private val mCoontext: Context,


                     private val mComment:MutableList<Comment>?): RecyclerView.Adapter<CommentAdapter.ViewHolder>()
{



    private var firebaseUser: FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
     val view = LayoutInflater.from(mCoontext).inflate(R.layout.comments_items_layout, parent,false)
    return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return  mComment!!.size
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
       firebaseUser = FirebaseAuth.getInstance().currentUser
         val comment = mComment!![position]
        holder.commentTV.text = comment.getComment()

        getUserInfo(holder.imageProfile, holder.userNameTV , comment.getPublisher())


    }




    inner class ViewHolder(@NonNull itemView:View): RecyclerView.ViewHolder(itemView)
    {
    var imageProfile: CircleImageView = itemView.findViewById(R.id.user_profile_image_comment)
        var userNameTV : TextView = itemView.findViewById(R.id.user_name_comment)
        var commentTV : TextView = itemView.findViewById(R.id.comment_comment)

    }
    private fun getUserInfo(imageProfile: CircleImageView, userNameTV: TextView, publisher: String) {


        val userRef = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(publisher)

        userRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(po: DataSnapshot) {
               if (po.exists())
               {
                   val user = po.getValue(User::class.java)
                   Picasso.get().load(user!!.getImage()).placeholder(R.drawable.app_ic).into(imageProfile)


                   userNameTV.text = user.getUsername()
               }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

}