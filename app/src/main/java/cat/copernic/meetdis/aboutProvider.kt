package cat.copernic.meetdis

class aboutProvider {
    companion object{
        fun getAbout(): aboutModel{
            return about[0]
        }
        val te1 =  R.string.aboutApp
        val te2 = R.string.propietaris
        val te3 = R.string.versio

        private val about = listOf<aboutModel>(

            aboutModel("$te1", "$te2", "$te3")
        )
    }
}