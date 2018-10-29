<h1>HackSheff-18</h1>

<h3> --- INTRO ---</h3>

This is a project that was created for hack sheffield 4 a hackathon at the university of sheffield. Our goal was to answer the challenge set by egress.com to create a hack that will increase security of personal data.

With all of us being registered to so many online services, it's pretty common to use the same password for multiple accounts. But what if one of these services suffers a data breach? You need to know all the other services for which you registered with the password that was lost, that's what our app does!

Naturally, our app never stores plain text passwords, instead only storing the hashes, comparing each hash with each other to show the user which accounts use the same password, without the risk of that password being stolen from our system.

<h3> --- USER GUIDE ---</h3>

You can build a list of all the services you're signed up for and the passwords you've used.

If one of these services loses your data, you can select that service along with the time of the data breach, and the system will flag al the other services that use the same password as the one lost, provided that you added or updated that password BEFORE the data breach.

You can then update each password, and it'll be removed from the list of passwords flagged as unsafe.

<h4> TRY THIS! </h4>

Add your own services
Select a service that has been breached, with the current epoch time (1540727513 at the time of writing) and see which services have been flagged
Update the passwords to these flagged services and see what happens
Hopefully, we could eventually be pulling data about website breaches from a web API, if we were to combine this with a web browser plugin, which would capture each password hash whenever you sign up for a service, the process could be fully automated! The user could receive notifications on any accounts that may be at risk, we could even hold a link to the password reset page to make life for the user even easier. We really think this idea is expandable!

Thanks for reading!
