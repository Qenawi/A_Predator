## A_Predator
<br />
(AP) Networking Library is a new library for doing any type of networking in Android applications which is made on top of
retrofit2 and kotlinCorotines / RxKotlin.
</br >
  Networking Library takes care of each and everything.<br /> 
  So you don't have to do anything, just make request and listen for the response.
  <br/>
  <br/>
  > simpleRequest Examble : ' OutOfTheBox and could be used any where '  
  <br />
  
  ```kotlin
        val Headrs = HashMap<String, String>() // add headers if needed for nw call 
        val Body = object : HashMap<String, Any>() 
        {
            init {
                put("api_key", ApiKey)
                put("language", "en")
                put("sort_by", "popularity.desc")
                put("with_genres", 16)
                put("page", page)

            }
        }
        val Url = "BaseURL"
        /*
               a_predator_netWorkManger =
                A_Predator_NetWorkManger
        */
        
        a_predator_netWorkManger?.FetchData(
            DescoverResponse(),
            Headrs,
            Url,
            Body,
            object : A_Predator_NWM.RequistResuiltCallBack {
                override fun <T : CService_DBase> Sucess(Resposne: T)
                {
                    val data = Resposne as DescoverResponse
                    if (!data.results!!.isEmpty())
                    {
                        Cache.addAll(data.results!!) // list contain data 
                        adapter?.submitList(Cache) 
                        page++
                    } else {
                        is_lastPage = true
                    }

                    swipeRefresh.isRefreshing = false
                }

                override fun Faild(error: A_Predator_Throwable) {
                    is_lastPage = true
                    error.printStackTrace()
                    if (error.GetDoAction()) {
                        Toast.makeText(this@DescoverFragment.context, error.aCtion, Toast.LENGTH_SHORT).show()
                    }
                    swipeRefresh.isRefreshing = false
                }
            })






```
