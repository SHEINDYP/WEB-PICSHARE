import {Typography } from '@mui/material';
import React from 'react';
import './About.css';

export default function About() {
  const deletePost = async (sharingId) => {
    try {
        const response = await axios.delete(`http://localhost:8585/api/sharings/delete/${sharingId}`);
        dispatch(deleteSharing(sharingId));
    } catch (error) {
        console.log('Error delete sharing:', error);
    }
};

  return (
    <>
    <button onClick={()=>deletePost(6)}>del</button>
      <Typography variant='h4' className='h1A'>קצת עלינו</Typography>
      <div className='divA'>
        <img className='imgA' src="src/pic/bckCamera.jpg"></img>
        <Typography variant='h5' className='pA'>
          PicShare -ברוכים הבאים ל
          האתר שבו הצילום הופך לאמנות, והיצירתיות היא ערך עליון.
          כאן, אנו מציעים מגוון עשיר של המלצות על צלמים וצלמות מובילים, שהם מומחים בכל סוגי הצילומים - מאירועים גדולים ועד צילומי חוץ וסטודיו.
          אנו גם מספקים מידע מקצועי על לוקיישנים מרהיבים ונופים מושלמים המתאימים לצילום, כדי שתוכלו לנצור את הרגעים הכי מרגשים בחיים בעיצוב, צבע ובסגנון.
          בנוסף, פרסמו את המלצותיכם האישיות ושתפו עם הקהילה על חוויות צילום מרתקות וייחודיות שיש לכם.
          אתם מוזמנים לשתף בטיפים, לתת עצות ולשתף במגוון המשאבים שלנו לגבי ציוד צילום, אביזרי נוי ועוד - כדי שהצילום שלכם יהיה מקצועי, מרהיב וייחודי כמו שרק אתם רוצים.
          PicShare הוא עולם שבו הצילום נע בין הפוטנציאל הטמון בכל אחד מאיתנו לבין ההשראה והיצירתיות שנוצרת בקהילה שלנו. הצטרפו אלינו והתנסו בכיף של צילום, שיתוף והשראה
        </Typography>
      </div>
    </>
  )
}
