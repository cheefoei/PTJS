package com.ptjs.ptjs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptjs.ptjs.JobDetailActivity;
import com.ptjs.ptjs.db.entity.Job;
import com.ptjs.ptjs.R;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobListViewHolder> {

    private Context mContext;
    private List<Job> jobList;

    public JobListAdapter(Context mContext, List<Job> jobList) {
        this.mContext = mContext;
        this.jobList = jobList;
    }

    @Override
    public JobListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_job_list, viewGroup, false);
        return new JobListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobListViewHolder jobListViewHolder, int i) {

        final Job job = jobList.get(i);
        String companyImg = getCompanyImage(i);

        jobListViewHolder.title.setText(job.getTitle());
        jobListViewHolder.companyName.setText("Connect Dots >");
        jobListViewHolder.workplace.setText(job.getWorkplace());
        jobListViewHolder.workingDate.setText("30 July 2018");
        jobListViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", job.getWages()));

        byte[] decodedString = Base64.decode(companyImg, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory
                .decodeByteArray(decodedString, 0, decodedString.length);
        jobListViewHolder.img.setImageBitmap(decodedByte);

        jobListViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("JOB", job);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    private String getCompanyImage(int i) {

        String[] img = {
                "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAUDBAQEAwUEBAQFBQUGBwwIBwcHBw8LCwkMEQ8SEhEP\n" +
                        "ERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/2wBDAQUFBQcGBw4ICA4eFBEUHh4eHh4e\n" +
                        "Hh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh7/wAARCAGQAZADASIA\n" +
                        "AhEBAxEB/8QAHQABAAEEAwEAAAAAAAAAAAAAAAgBAgYHAwQFCf/EAEoQAAEDAwMCBAMEBwMJBwUB\n" +
                        "AAEAAgMEBREGByESMQgTQVEUImEVMnGBI0JSkZKhsVNyghYXMzdic7LB0SQmNkNUY+EYJUR0g/H/\n" +
                        "xAAaAQEAAwEBAQAAAAAAAAAAAAAAAQMEAgUG/8QAJhEAAgICAgIDAAMBAQEAAAAAAAECAwQREiEF\n" +
                        "MRMiQRQjMlFCYf/aAAwDAQACEQMRAD8AmWiIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiALj\n" +
                        "ePmyfu4XIsS3Vvz9PaSq7hGfnDCB+KmMOb0Slt6PD3O3VsujWmnnmbJVH7rRyvd26v02odOsuNUA\n" +
                        "DKflwPQ9lAHU15rr7eau418rpyXnpBPblSU8LW5VNJbRp+4ytbK3iPqP/Nend49wq5Ivdeokj2A9\n" +
                        "fSXA9K5cLhhDR+lDg4O9Qcrl6mg4yvK5P9MvFlzVVWtIOcKuQieySqIEJwMqSQqFOoe6o7vwQob0\n" +
                        "CqphW8q5RvZGirRyqq0d1cpQXQREUkhERAEREAREQBERAEREAREQBERAEREAREQBERAEREARFY5x\n" +
                        "DgPRAXq3qXG6Xpa5xIDW+qxjTWtrXfblPQ0czHTQuLSM+xXUYOS2iUmzKwclXLha5xPA7d1eHO/Z\n" +
                        "K4TIL0Vhc4fqkqnW79gpsjZyIrC8/slA845BCkkvWvt+7dNcNAVccOctaSQFn/Vjkrr11PFW0slP\n" +
                        "NGHRyNwQV3XLjJMmL09nzOqGltVUxnLHxPIIPquS111VbqltRRSviqGHqGDjK3T4h9q6qy3aS7Wm\n" +
                        "ke+mkJLgG/VaPd1dfSQWyDufZfWUWRuqUT069SiSi2Q31gmp4rTqKTy5B8oe4qRVtr6W5UjKqncy\n" +
                        "SN4yCCCvmm7hwMcnztOeoFbQ2s3jvulauGnqKg1NJkAtkPAC87M8d+wKLKtvonVGCAcnPsrlhWgd\n" +
                        "xbBqqjY+jqWNmOA6Mu5z9FmQc71x9F4coOt6kY5wcTlHZUdnHCp1cKjnEDJxhcKS3ohFCA3nGSVx\n" +
                        "l7Gn5nMDv7wWA7w7kUGibI90j2uq3A9LcqKl93p1bcquR8NY6BjvuBrj2W2jCst7RZGtsnW2Vjhg\n" +
                        "SscfoVyBaF8NLNRXi3i9Xi5VEsb+WDPBW+RjGFnur+N8WctaKjurlaO6uVSWjkIiKQEREAREQBER\n" +
                        "AEREAREQBERAEREAREQBERAEREAREQBWPGXfTCvXHJjqbn3QGG7sXiSyaErrhC7DmNPKg/Yda3my\n" +
                        "6kmvFDVujkdIXlnV3ycqdO6VifqDRdfaoh80zCAvn9qW11Vmu0tBWQuYWSOaCR3wSF7HjVCS0zXT\n" +
                        "xfTJEac8S72UkcdyovMkx8zjnCyGDxJ2YtzJRsH+IqI2OenoPSPXKp04/U4XoS8dVLtGiVEfwmAP\n" +
                        "EjYP1aZn8RT/AOpGxf8Ap4/4iofYYOPLVQxv6rFU/GVnHwRJfDxIWJxw6mj/AIiu1D4itNcdbGMH\n" +
                        "v1FQ36f9kKmMjHQFD8bD8I+BE5rbvtoepc1s1xZG93YErO7Hqa0Xun82hq2SxngOB7FfN5rC09QA\n" +
                        "yF7Fl1JebVO19LXVEbGnOBK4DP4ZVU/E7XRDoR9FLlbaS6UbqSrayZju2RlR03Z8PctTUPuGm+O7\n" +
                        "jEB3WMbdeIW7WypZBex59K3jPcqSuiNe2LVVIJLfVsDiASwnlZON+I+ijU4MgRqTTV109XPpbpSO\n" +
                        "p3+xHdeP3YQQAV9EtaaJsmqaV8NfTROc4ff6B1fvUU95Nl7rpmpkr7bG6qt5JJDRkhejj56tepmq\n" +
                        "uxP2av05qC7WCtirLfVvZI1w4DuMKVmze+NFe/ItV5cGVeAPMJ4UP3RmOUtcCOn9U+n0V9NNNFN5\n" +
                        "8Eront9QcFacnEruW4nc4Rkj6awzRzNDmnIIBB9wrpSWMOBn6KLHh+3pfFUR2HUEx6SA1kjjn+al\n" +
                        "BSTtqqcTRPD43N+Qj1XzV+NKmXZ58oOLIVeKa4z1OvpaV0hdGz9XPZahaelzckFvfI9Ft7xTW6Sn\n" +
                        "19JNKC0SAAFahORIWkDA7L6XBinT0baopxJr+Fetjn0BSUzHAmBpDv6rcgUSPB9qptHdprJM4/pj\n" +
                        "huSpagfJkFfO58GrGZLlpl4VysHLgVesiKEERFJIREQBERAEREAREQBERAEREAREQBERAEREAREQ\n" +
                        "BERRsBWO+92yMK/CtKjtgtwTjIWq91NnrLrQuqowKeoHsMZK2sFx5Ad8zslWU2Tpe4nUZNeiGt98\n" +
                        "Omq4JnfZkfxDB2zJjK8CXYjcRsnR9ljH++Cmvd9RWa1sLq+4QwY93LELnu5o2hLuq5RyEexXpQzL\n" +
                        "5LpGiN1miM1v8PWu5BmooWxgn+2C9iPw3aicwE8O9utbgm8QGlY3ODS6QA92uAVafxA6TlOCJR/j\n" +
                        "C7+bJf4dqdjNL1Xhz1S1maeHrP1kwsbvOx+vLY10ktsLmD9mQFSkt28ujqshrrm1hPoSsotmrtOX\n" +
                        "cBlPcIJC7sC5P5OTD2iPmn+nz6uViu9vndDU2+qY5vf9E4j9+F5srXMGXxvGDy1zSF9Hq2wWS5wn\n" +
                        "zKKnla7u4AFYVqTZTSN3gk8ulMUxHDv1R+S1VeVgupezqOR32QVIbgSsHb9XuvY0vqa7WCvbVW2d\n" +
                        "7S05MfVgFba194fb3ZpH1NnkFXGTwyJpB/mtN3mz3C0VRp7hSy00me7mlegr6MiOl7NHOE1ol7sb\n" +
                        "vRQaobDa7pK2nr8EAOGAcfVblqaemuFK+OVrJIpG46TyCvmvb6uehq2VNNO+OWM5a5pwpZ+Hbd2K\n" +
                        "+UsdivLwytYA2Nzj3XjZeFKEuUTHKnXaMV382V+FZUX2wxEt5MjG+hUb5o3xymCWNzXtOHA8L6ZT\n" +
                        "ww1dK6GaNsjHjBB5BCin4k9pfsx8l+s0RMDiXSNaO3Ks8fm/bjI6qs/JEeaeR0ErXRkl7Tlr88qX\n" +
                        "Hhf3MfeqJtguEuKiPhnV6gKIwZ0vw0EOb3BXtaNv1XpzUNLcaSVzCJR1AH0zyvRzseN9fNFtsFIl\n" +
                        "F4s9HC7WFl5pY+p8DiZCO4GFERzXh2PVo9V9BbHV0et9D5IbK2SLDh35woV7vaSrdKawrYZ4i2Cd\n" +
                        "xkiOOA1ZPG38foyKZaejz9u726wato7g15Y0OHUR+K+g+m6+OvstNVMkD2yxNdkH1IXzWbhgBB+Z\n" +
                        "vIUu/CPq6W5affZ6+fqnj+5k+nouPJ0LXJFd8N9kgWHDun1V644zk/3VyLwDEgiIhIREQBERAERE\n" +
                        "AREQBERAEREAREQBERAEREAREQBD2VB3Rx+U5XMewUBd64x9Fa5/PIK4KqphpKaSonlDGMGTlaO3\n" +
                        "Z34t1mp5qO0SNnqOkjqB7H2WiqiVr4xOoQ5PRtjV2r7LpmhdU3KrjaB+qHcqOm4niNqp2SU2nI42\n" +
                        "tJI8xxPUtIas1dfdUVD57lVyCNxOGdRwseYHkEDAavcxfGKPczZXj6Pcv+rdQ3qodNX3aolDjktL\n" +
                        "uF4sks0hy+ZzvzVGjjDGdZXr2PTV5vEoZRUjnE9uFuddNXou4JHkBxA+/wDkVUPA+6efoto2bY7W\n" +
                        "VcR5lLLC0+zfRZha/DHd6jBqbq6An0LQqZZtMOjn5IxI/te5jupsmD9CV3qK9XmkeH0dzqInDkdD\n" +
                        "uVI2Pwt/L818eT/daulcPC3Utjc+C8vkI7NwOVSs+l9Mh21sw3Qm++prD0QVTjUwg4JlPKkRtrvR\n" +
                        "p3VRZSyTtgqyQOlx4UdNRbC6stnV8LTy1Ebe56Vrystt70vcQZY5aSZpyDyFTOii/uPs4lCNi1E+\n" +
                        "jrXw1LeA2Vh9RyFhm4O3Vg1bQOino44pT92VreVozYre+amljs2oZf0JwBK4qUFBV01dRtngf5sL\n" +
                        "gHNcPqvLlXPHntGZp1sg5u5tNedHVbnwwST0ech+FgFputXaLlDcKd7mTxPGccHuvo5erRR3m3yU\n" +
                        "ddGyWKQYHUOVDff7aqq0rdpLpb4+qieckY4C9bFzvmXxz9l9Vya0ySGxWuodXaajc+VhqYQGOAPJ\n" +
                        "4WeXa30t0oHUtTAyWJ3drh3UGtgdYTaY1rAHSkUlU8NLc8dSnTb6hlTTRSRu+VzQ5edl47onyRVd\n" +
                        "DXaIN+IDQUuj9TyzRseKGdxLHY7ZJ4WsmD5Bg56cnPqp/b06Po9U6Sq4pIA+ojYXRHHOeVAy70Ut\n" +
                        "suT6GVhE0Ly14+i9nx2V8kOLNNE+a0Sg8HmqG1dvnssso6owHMyeScr1PF1pd1w0z9rxRdU0I6ct\n" +
                        "Hp3Wg/D9epLLuFSNjeWtnk6SMqa+r6GC86OqoZmhzZISRn3wvOyovHvUkUT+th85HNDXdA5J4WxN\n" +
                        "g9UnTeuIAXjyZntY4vPbPHCw7U1sfZ9QV1BN9+KRwZ+ZXQopHQ1EUzSWviPXkfRe1dD5sfZrceUD\n" +
                        "6Y2+ojqqaOeFwdG8ZB912lrvYS+C9aFpJC/qcxgBWxF8jZHhJo82S0yhJHplAeOVQkB2S4ALqPr6\n" +
                        "VkhY+QArjRydwZ9cIDkriZJFJ91yvGQ7HogL0REAREQBERAEREAREQBERAEREBQ9lRVd2VAud/bQ\n" +
                        "/DilB6s9XAGcK1z/AJch+OcFUrW9cL4wS0ubw72UY9dbzXzQ2410tMlL8ZBHIOgOOB2+qvpodkuj\n" +
                        "qurkSf6gGZL+PdYdr7cWw6Vt01RU1TJJI8ARNPJJUYNW+IfUt4p3RUdL8Fnj5SD/AEWqL3erneKj\n" +
                        "z6+skkkPOCeF6VHinvbNMMfRs7dbey86pEtFQyOpqXJA6SWkhaidI6V/XJ1SSepcc5Vv3j0lv5he\n" +
                        "ppqw3K/3SO326F75HEDIHA/NerCmvHW2aVFRWzzYm+fJ0Rtd5nt3Wf7e7Vak1ZUAR0r6aH1c8YyP\n" +
                        "flb62e2Ft1pgZX34ConcATGeQFuuOO02KiPS6npY2j1IbwsOR5F74wKJZD/yjTGiPDtYra6OW6vM\n" +
                        "84OSAeFuS0acs9siZBSW+mYGDGRGM/vwsF1TvTo6yPdG64eZI39lhdytYan8TkjJHQ2y2CQH7snU\n" +
                        "AsLjfcytxmyTrGBuWtaWY7YCrh36znH8GqFVf4g9VVBcY+qIewf2XUpt+dXRuLnVD3D6vR+Ptj2y\n" +
                        "PhkycILT6k4VzSCeDwoi6f8AExd4HBtbb2FnYu6gVtzRe+umLyxjKuqFPM/0LOP3qmeHOHZxKuSN\n" +
                        "uua1wIdyPqsT1xoayantr6OqpYQ93Z4jHUPzWRW+4Ulwpmz0lRFMxwyCxwK7DuPmaA44ws6nKp7K\n" +
                        "lKUX2Qr3m2auGkXG4Wpkk1IOSWnJCzTwx7o1Pnx6ZvDy1vaMvPP4KStxtkFfQvpatjZon9w4KI2+\n" +
                        "GgKzQepIr7Z2ONKx5lY4H1zkjC3xyFbDizSpckTGa5py4e2fyXha309Tal07Nbp2txIw4JGfReHs\n" +
                        "xqr/ACo0dS1b3A1AYBMPb2WccY6O2BwsL3XIo1xPnhrWx1Gj9aTUjmkNhqMxk+3upsbJ3dt529td\n" +
                        "V15kdHzzzxwtJeMDT7YZobyyLp/R4c4epyvf8HF1kqrDUUL5CRS/IB7ZGV6uQ1ZQmapvdZITpEgc\n" +
                        "HYPGDlQt8VOjRp/V/wBpwcx1mS7HYdyppDOXkfePGFo/xaWZlVoqSuc3qdD0jPtlZMCfG9Ippf2I\n" +
                        "o6Cn+G1hbZ8/rtX0J0874vS1KTyHwDuvnVYMx32iA7skbn96+iGgXF+jLU/0dTt5W7y0vsmXXvsh\n" +
                        "h4kLd8BuNXyBoAlkBHH0WsHDJABUgvGVbW0t9pKwNx53OfflR8JIY1w7kr1PHy506L6u4krvBleR\n" +
                        "LZ663uceoH5QT7FSL6hhpecHGSodeEGtfHrJtI0kNk6i5Sm3Au4sulrhcS4NfFCS1eBm0/3aRlth\n" +
                        "uZqDf/egWGWax2V4dU9B6iPQqMNdq/UVXWOqJbnWNe85IE7gB/NdXU11lvN8q7lMS6SV5dk+gyuh\n" +
                        "Cwve2OMdYf8Areq9bHxFCvbNNdaSNxbTbv6rtdSygMVXXwtPPQ0ynH4qXWib46+2iOukpZqV7xyy\n" +
                        "Vpaf3Fau8Om3Fvsmmo6+qh82rmAcHZxgELdUMbI2hobjp7fReJlyTbSMlrRyhVb6qg7KrfVY4+jO\n" +
                        "iqIikkIiIAiIgCIiAIiIAiIgB7KiqmFGkQcMx+ZoP3Sor+LLRNWa5t9pacyNlBMhaOe6lW+MP7kr\n" +
                        "qXG1UVwp3U9bA2eM/qvGVdj2uqW2W1z4nzPLHNl6D1NI7ggqrI3ySdLRI89hhpU39RbC6Qu1SZgJ\n" +
                        "aYk5xEBhd3TGyukLMQfhfinD+2aF7MfJQUTU70RM282w1Lqi4RU8VJJTU5ILpHNPIUt9tttrHoe2\n" +
                        "CQxROqR80kr/AEWZSR2jTlufL5cNNBE3OcY7KL++u91RcpZbPp+YRwNBa+Rh5JWdWW5b4x9HKlKf\n" +
                        "SNobq712XTDXUlsmZVTdnhpzhRl13ulqfVT3edVSRw5OA0449FhNXNLU1HnzSOc8nLnE5JVIYpJn\n" +
                        "tjg6nOecBrRyvTowYVR3MtjVBLbBnllceqZ7ye/Ucr0LPY7tdHiO2UD55HHGelbb2k2Kud/dHV3x\n" +
                        "r4KQnqHQOT7ZypR6R0Hp7TtHFBSW6EysHMjm8lU5GdXV1A5lcodEWtF+HrUF1iZVV0jKME8sc08/\n" +
                        "VbFovDVb2tHxVZE4+uGlSI6Qz5WsAH0Crg+q8qzylsnozyypfhH+fw22d7C2KpiaffpKxDUvhtu9\n" +
                        "HTyz2q6NJYMtY0EEqWDQUc1rh0vHUFUsyx+yFfL9IV6S1JuDtvW9FdRzGhY/pc1wz1D3Uo9ttd2n\n" +
                        "Vlpjnp5GxzHAfGSMhy9y96dtN4hey4UMMxIwC9vZaXvu2ty0VqFuotMzTy05PVLTfq5z3wk5xsWm\n" +
                        "RKaktG/R6grHNwNN0mpdOVdvqYmukljLYyR2K5dF6givVrDy3pqWACSM9wV7jvTPclZ47jLo4i9G\n" +
                        "gvDY2psF5uWl6xxDqeQiPPqM/wDwt/4HBWsaizOod36CqgYWxzNkMrsdz0rZoHyNC7yJKWiZvZqD\n" +
                        "xWUDarblzgwF4l7/AEwsF8FkT2tvPWCGl46T/hWzvEg5se3c5d+1hv44Xi+FaxOoNDxV80ZbJVZc\n" +
                        "7j64WiM/6dFil9dM3LnAP4LXPiLpmVO2VeHezT/NbGOTkDGFrXxH1Qp9r7gSRn5B/NUYzavRxT3L\n" +
                        "ZCTTsJk1PTxDlzpRj96+huhYjFo61REYLYGgqBO1dC65bh24AOMZqMce3oV9BLVGaa3QQEACOMA/\n" +
                        "ivQ8nJNosvf6Ry8a1MDDapT6A/8AEotHgtB7ZUq/GtIBa7UP1nZ/4lFYnkA9gvT8Vt1bNVD+pvLw\n" +
                        "gQOk1y2YN+RjX8/ktv8Aiuu0tu0TDHE8g1Tixw9+FhngvtRNPXXGRpBYfk478r0fGPOBY7cxxwRK\n" +
                        "44/wrBY+eT0VTe7OiJ5dgPHrlZJtpanXrWtvt7W9TXuGePqscI+dwA//AMW4/CpZ/jdwG1hBIpXA\n" +
                        "t44PryvUy5uFWkXTlqJMex0cdFZqamib09DGtP5DC9LtwjWDpx25yq9PJOe6+Sk3Jnmyex+qqt9U\n" +
                        "xxhVAwiIQREQBERAEREAREQBERAEREAREQBERAceAM4Vkr44o3SvOA0ZJV4z1O+i1R4idwGaT0vL\n" +
                        "BSuBrZwGtH0Pdd01ynLijquLkzUfiY3XqK2sk01apCynBw+RpUdy5xecnHpk/rfVdi4VL62tqaqo\n" +
                        "c4ySuJ5OVxU8Ek80cLR1Od8rQO+V9bi48Merb9no1pQWzkoKaern+GpozLI7spSbC7KQ0lLBe9QU\n" +
                        "+anhzI3D0KeHDaEUcTL9eYwZnAOja4ZCkNNPT0FPJNO9scMbe54AwvIzM1yfGJmtsbekX09PBTRC\n" +
                        "OBgiaB90DGFwV90o6JpfPKGNaOSVo7dPxA0FnEtHZHtnqQekPAyAo7X/AF1q3Vd1MfxtQ6WV3ysj\n" +
                        "cQP5LHXgTm+cziFbfbJk3XdrRVue9lRd4g8emVjsu/8AotshaytY/H1Wh9J7I6q1GGVdYXRRk4eJ\n" +
                        "O5+vK2pp3wzachYJLo973+vSVZOuqtaO+MF7M3tW92hq35X3ONjj6ZWZWbVdjuzB8FWseHdjlYNQ\n" +
                        "7G6NpsdFP27cL0Ytp7DER5E9TCB26JCFjko/hXJRNiRvDmZB6mnsVR7A4Y6Q4Hgh3ZeZYbP9kQCG\n" +
                        "KaSVg4+dxP8AVes091U3opPKorPSUdfLUwR+U53LiOy9IOHSOrk+i5MZHumPoFz37Gzp1FFFLVx1\n" +
                        "LmjzWcNK7IGDhXJ6lG9onZqfxHE1un6O0Rn55qgH8lnGg7V9j6Vt9B0gCKIZ/quG56dZcNSQ3GbD\n" +
                        "mws6Q13IznvhZGAGMw3kAYVzlqGidh5+Tj1Ue/F5fhSWD7KY7Mk3ScfQclb2u1fDb6OerqHhjI4y\n" +
                        "/J7cKFWuK66bo7m/B0gdJFHLhpB4xnlaMKOpcpFtUddmY+EfRbqy5T3+qj/QBo8nI9cqV3Hys9Qf\n" +
                        "mWMbX6Yg0rpimtzAOtrAXYHqsmlf5cEkrsdsj8Fxl2O23o5m+T0Rc8atxZPVWylYf9GD/wASja1v\n" +
                        "mENHcnC254oLsK/cKrhjdkQSAAenZYJoCxz3/VFDSU8Rc6SUCUD0GV9Bj/0Y5sj9YEv/AAyWX7L0\n" +
                        "FHMAAZ8ErV/jJubH1lJRZ+68/wBFJHTFuhsWn4aFjcMpogSfy5UKvEnqBl73Dm+Gd1RRnB9gvLxd\n" +
                        "2XuRRD7T2azzjLz7YUm/BrbwySvqiPmJGP3KM3ThmHfd6u6mR4SrSafRbLiRgVTc/u4XpeSlxr0X\n" +
                        "XPSN6N7Kqo3sqr5g88IiIAiIgCIiAIiIAiIgCIiAIiIASqAoeyoEBXKZCoFQlOiFtnVuFZFRU0tV\n" +
                        "M8MZEwueT6AKCW/ur5NT61nMMpfSRHEbvfv6KSXiZ1hHYdJyUMcoFTMOkgHnBUK6iQySGVxJMh5c\n" +
                        "V7visXcubNuND/pbI7AcSMloBK3l4adsnX25svlwh/7NG8BvUe/5LV23unKjU2qaS2QguYZR5hA9\n" +
                        "FPLR9lt2ktMQ0zGsjbDGHPd25WjyeTr6x9nWRLS1E9G4Vtv0/ZuuolZBTQtwHH0/JRH3w3luF+r5\n" +
                        "aGz1LoaMEsOOOrHGV2PEZutNeblLY7XK5kTCWuLT3WkbfST19VBSwh0s0r8HHPqsmHhp/wBkyK6k\n" +
                        "lyZ6Ol7BcdTXaOkt1O6aWR3zucf3lTA2l2as2nqGnq62Bk1c4B2XDPSVzbBbaUml7BHW1dO19XK0\n" +
                        "OGRyMrbTQ0MBIwR2CozM+UnxiV2X96RZTwsij6B0tDeBgYC5PunGc59wrXOaxpdKWhvfJPZa/wBd\n" +
                        "7sac0wHQuqmVVQP/ACmHJXnQhOT2UdyNh9bc9ODn8ED2Z6Q9jj+znlaRs2stb6wfmxUE1tpyfv1P\n" +
                        "zAj6YWz9IWitoYBLdKttRVnuWAgfzUyhoNGRDP4KuFUdkVZyU5TlVRSRotIOEP3s+iuVPRRroFmQ\n" +
                        "HlvbjK4zLHFE58jw1o9SVfMAWc8j2WGa4orzfGfZlvc6kiPBkXcUpdM6RqrfLW1x1BXDSWlmvqfN\n" +
                        "d0SyMGABnB5WZbI7W0mkLdHWTsD7g8EuLuSMrJtEaDtOnoPlibNVHl0rxk59VlxfjDRzjg4Vlt3F\n" +
                        "aiWO3S0ijW9AAxknufZY1uPf6XT+k6ysnmDAGFrD9VkNRPHTwSvqXNZG1uckqI3iY3LivNZ9hUEp\n" +
                        "+FiyJC093LrGh8kkKoOUts0vqa6T3u/VVxqHEySyH+vCkd4RNFuibLqKthIMgxH1Bae2R0FWaxvs\n" +
                        "X6B4omyDqcQp02K1UtntsFvpoWxRxRgceuAvWzsmMa1XE0TsSWjx90b1/k/oytresDEZbn+8CF8+\n" +
                        "bxUuqrpWzPd1GSUkn2UkvFrrlxp49P0UoLXu/S4PsoyO6BIcnPV3VvjKOMeTGPW12zsWyD4isp6X\n" +
                        "JcJXhrcepX0A2dsxsmgLdbnRljo4xkfjyoWbK2T7Y13SQ4LmxuD+31U/aNhipo4RgNY1rf5LL5a5\n" +
                        "SfEjJkdsEYTKcKi8TZjK5CqDlWqrfVdAqiIgCIiAIiIAiIgCIiAIiIAeypjhVKp6IC15LW5AyunX\n" +
                        "10VLSvqZSGxsHJK7FQcY+bAWifE7uFHZrFPY6KbpqpW4BBV9FPyPRZXHZoDfjVs2pdbzyCcvga8t\n" +
                        "6QeB0kha9B6g6M9g7j80c+SQmRxLnuPU4n3XbsdJJX3alp42dXXK1pH5r66rWPRs9BaiiTHhE0Sa\n" +
                        "aKo1BUxdQkAEZI9crIvE/uC2x2V1loZA2qmbk8847LN9IU8Gjdtg6bETKeIyO/MKGO7GqJ9T6vra\n" +
                        "x7zJE1xbEc9m5Xh49bybnJ+iiK5T7MUmlklnkkmd1vfkl/qt6eFHQbbzfXXivjd5EHMeBwT9Voqm\n" +
                        "hMs0cbDl7z2U9didNHTmiaaDpAklY2QnHuAVr8hf8FfFHVr0ujP2N6GBjMBrRjA9MLr3GupqCidV\n" +
                        "1jxGxoJJJXYlc2JrpC7DQMlRU8TO6dVUVsmnbROWQtPS97Svn6KXZLbMlcOTL9597Ky5XGWxaVkI\n" +
                        "YP0bntPzl30+i9DZLaCe6ubqPVZnlmf8zQ/1XieGXbg3SuGo7rAJI2PDW9Qz1euVK+njjgh8qJoj\n" +
                        "YwYa0Ba8ixULSLLNQOG12+kt0ApqSnjgaAAOgLuDOQDnP8lxvPlxAuOcck/RYzbdTSXq/TUdtb1U\n" +
                        "1McTPHoVhUuRSZe0+6qFZHlsY6u+Fc3so/TkqiIgCp6Kqp6IR+lpHzZ9FZ0tceTn8VZXVEVNEZJp\n" +
                        "A1o7rDr9ubpOzgiqrmNI+qQrbOkZi44GCc/T1XSulyorfAZ6meOmjYMu6zhaT1R4h7bTNey00nxE\n" +
                        "h4a8ZWq7vWbn7m1bvhopvg5D+oTwFqqxGu5ltdX6ZXvzvWJ/MsunpQ5p+V8jjz+WFqfbrb2/6zv7\n" +
                        "JXwSGndJmR7h3W39vPDrP8VHW6jmMgyHFh7qRFi0/bLJRx0lupmRBgHIatn8umqOorssdqgjzNuN\n" +
                        "H23SNljoaOICTAL3uHOV2NfaipNNWCpr6yUAsjPlgnuV7VZUxU1K6WV3yDu5Q98Se5keork+0WqY\n" +
                        "up4CWvwfYlZqKJZNnJnNcHN7NWa4v82odQ1Vwe4Oa+QkDPAH0XhkNIDcEkkAK2PAAI7OPC2HsvoO\n" +
                        "u1dqdhMWaSNwLjj6r6OUlVXo3uSjE3V4T9AmjonX6vid5z3jy8jjpwpDS1EMEUk07hHGDnLjwvIp\n" +
                        "GUGltLxtLmxwU0fzfkovb4711V4qZbVYagx0zCWve0r5+NTyrTDx+Rm/9S7vaPsUhZUVvm47+WQV\n" +
                        "0tP736LvFUIIKp0bnHDeshQZlqqid5fLO95J5Lnd1ZC6SJ/XHM6N45DgfVegvEJR2y6OOtH0xoqy\n" +
                        "KrpG1EL2vY4ZaWldmPPTkqNHhZ3JqatosF0kMjxgMJOVJeMAA4PdeJfV8c9GOyHFlyIiqOAiIgCI\n" +
                        "iAIiIAiIgCIiAtk+4ecLp19UaSglq3MLzGCcBd14y3GMricwva5r2gtcMEFE+yF7NC7g+IC226mq\n" +
                        "KGkgeyuGQOpvZRZ1bqCu1JeJLjXVDnvc4kA84UoN89jhfXyXayNYKnpOY+2T7qNNz0TqWgrTRS22\n" +
                        "VsjTjqDC4f0X0HjvgS3Jm6mSSMdy4NDj90nt6lSA8LG3jrlcm6hrYC2KAktDxwcrobU7EXi8TwVt\n" +
                        "7gMFMxwdgu7j8FLHTlmorFbYqOhY2OJgA4Hdc52cpw4IWWbNceJm9ts2g/hgen4kuiH5DKhKHPcX\n" +
                        "OOMOB/qpFeMm+OqLnTWZjsCA+Y4fiMKOhHp1fKDwtXiocamzupdGbbIWZl61/QUcrQWdYzkKflDE\n" +
                        "2mpGQjAYxgYPyGFDfwk0IqtZvnLcmA8n2UzWt46SPl9F5nk5crNFWSzXO/OsBpfRtVJE8NqnM6Yx\n" +
                        "75yCoXaap6jVGtaSGpJkdUzAHnP1W1vFtqz7T1Ky0QOy2n4kb9V5XhTsDLvrp1U5uRRsD+fQk4V1\n" +
                        "MPjx9s7guMCXmh7PBZ9N0dHBG2PojHUAMZK9wlhHVjkKyIdDGjPYdK8rVF3jtFlmrzjpjBLiV47X\n" +
                        "ySMr+zNY+IDXc9pigsFpk6rlVnysNPIDuFmu0lhdZdLQmYH4qdgM5PclRy2sE24O78t1uMjnR08z\n" +
                        "nRk+zTwpeQ9LW9DRgBW2xUES1oZHlnrOA3uVzRuDmhzTkHssa3Cuf2Tpapqerpd2C7uiqp1Zpagq\n" +
                        "XnLpIgT+8rO962V6/T2VaheGtJPYLijnikZ1MeHAnHChMg5HAkYBwrRg/eJyFV5PS4N5cBwFh51z\n" +
                        "b6K8m0XUPp53HDSWHpP+Lsuu36JRldTTsqIjHLgtPuFjldoTTtW8vqaKKUn3jBWQQ1tJM0eVUQvG\n" +
                        "P1ZAVyskbgkHI+ilOaJ0zGqTQWmqZ2WW2kOPeBv/AEXvUVBQUUfRTU0ULf8A22Bv9F2DgDq9FZJK\n" +
                        "xjfne1v48Jzsl0QnNnI1vpnAXDVzxwRumfK1jWD5i7theDq3Wti05Qvq6+uhaGjgNcCf3KMG72+1\n" +
                        "Zfo5bfZsw07gW+Y3glaMfFlZLRdClyfZk3iI3jjMUth09N1dxK9p9VGN7nSTPdIcTuPUSfXKvlfJ\n" +
                        "PKXO6nyvOXHuStj7T7T3jWlQyeSnkipGn5pX5acfgV9BXXXj19s1LVa0Y/tzom6awu0NNR07vJ6v\n" +
                        "0j8cAKbe22jLZouwshpmjzPLzI8j1XPt7oe06PtDKWiY3rA+aQjBK114ht2YdN0clntb2vq5AWnp\n" +
                        "5x+a8i+/+RPjD0USlzl0YL4m90pJpH6ctUv6DoPmuacHqz2UcAHEYa4dTzkk+q7Fzqpq+ukqql5d\n" +
                        "JK7qdk+qsggkqaltPDE50noB3XtYtCx6+TNNa4oUcEtVUCOngfJIT0ho91vzaLYOa7UzLjqIOZA7\n" +
                        "5gzsfosu8PmzUdEyPUF6jBmLQ5kLuRyFIemhaxgbG0MZjAaPRefm+R59RKZ3ms9O7Nafsd0prjQF\n" +
                        "7HR47HGVtSMYb0+g4XH0Ow0dXZXx9WXdXvwvFlJy9mNy2y9ERcEBERAEREAREQBERAEREAVO6qgC\n" +
                        "jsHHIAXdOAePVdN9roJH9ctDAXe5au+5ufXB91QMGME5/FSt/wDSds68cQih6GtYxo9AFcWhzR0j\n" +
                        "se65TGO6p5Y/aIHso23LZG3shD4q5pJNz6iMngRj+pWpe3UO+Ft7xXwvj3Lllx8rox/zWoAcPkHd\n" +
                        "fYYGnT0elT3EkV4K6Zsl0ulRj7pH9FKS61HwlvnmJwGMLs/go1+CVgAu+P2h/RSB19IYtH3F4PzN\n" +
                        "p3YP5FeDl936Mtv2mQJ3RrZa/XtzqHv6gZDj95W7PBXTs+17rLxnyG/8Sj5qGQy3mplcclzuVvzw\n" +
                        "U1TTfLxA8gYgZ0/X5l6mVHjR0aZr6dErSOQPrlaf8Tt+Np0JLRsf0yzsIHP1W4CCQz0UZfGzUObH\n" +
                        "a4muIDgQf4l4WDBzt0Y6VuWjq+DalbLPPWPGXdLslSkixyfoFGXwZSMEM8BPzdLlJluWgt47Jnbj\n" +
                        "LTJuaT0as8StfJSaOb5ZwHvGVlW0dW2q0Ja+l2emANP48rD/ABP0slRt+6VjSfIPUcLq+FjUUV20\n" +
                        "a6nEjPMhl6enPOMLuNf9HIJbhs3M7lpC1TTaon0vuA+yXMH4SclzJHdhlbUaQ4EjkLWe/wBpaa9a\n" +
                        "aNVRdTK6mBka+MfMcc4WeCTeitafRsiORpYJISJGP5BBXkan01a79Tupq2IEkfLI3uCtQbE7rMnD\n" +
                        "dM6glbT1sQ6WuecZx7re1LMyWESQlha7kEHgrqcZ1vZ1KDRHXWOhNfabnkqtM10lRTNOWsJJKw6f\n" +
                        "djdGwvEdzoHRsbwXdCl65jXO6nt6uMYK8m56asVyBFwtUFS0/ttXdWRB+0SrIr2RQn8ROqQDHHAQ\n" +
                        "fctWO3je3W1w6mOnLGHuACpS3XZzRdc4uFvigz6MaulSbGaNgk6zStk/vBao3UotVqRDO4XHUd/m\n" +
                        "ImkqZy88NwcL3tM7X6tvc8cUdufC12B1Fqmva9vdI21gMNlo+ofrFnK96lpaSjYfhoYmMHowYOUW\n" +
                        "bxf1Hz/8NHbXeHy32oMrL64VE2MlhC3VR0dts1DiFsdPAwYLhxgBebqzWdk03b31FfVxseBw3q+Z\n" +
                        "Rc3f30uF+Mluscnw9MSQSw8lcxjfky+3o5SnNmxt897aO0wzWmxSmepcMCRp4HfKileLlW3a4y1l\n" +
                        "ZUOmlec/Mey4JZ6id75Jp3SPJ56jnKsZGXHpjGXE4AH3l7ePgV018jZXUorbKRtkne2FkbnvJwMD\n" +
                        "1UnPDbtEXBmob9T8jBjY4d15Xh32gnraqC/XiIinBBaxw4P1UraOCKmgbTwMayOMYaAOF5/kM7a4\n" +
                        "QM91q9RLqeOOKARwxhjW8ABcoAOD2wqhvrlVwvFMXYx9VUKmFUcKCQiIgCIiAIiIAiIgCIiAIiIA\n" +
                        "iIgCIiAKgKozuqeqj0R+kRvGTbXRXqnrmj/SOIz+Sj63kk/RSw8alEJtPWydvBZO4kj+6onnhhPs\n" +
                        "vq/GS3To9Kj/ACSV8Es36e7t+o/opAblZOi7pj/07v6FRk8G1wFNqStpXHBlcMD8lKHXEPn6TuTD\n" +
                        "3NM8fyK8jLWr9meX+z523Xi4zZ/aK2X4Zb59k69ZA12BU4Yf3rXmpYfLvNTCR9wrtaBuP2Zq2gr3\n" +
                        "HpDJGj+a9myPOg163E+jrTlrXD1AUY/G3TkC0y+mD/xKROnaoV9hpKwOz50AIwtSeLeyOuGjhWsG\n" +
                        "TSMLl8/iP47jBV9bDVnhBvENLqmSikdy5jsKX/DiTnuBhfOXQF8msGo6C4wkjy5ml+DjIyvoBo29\n" +
                        "w36ywXGnIcJmfNj0V/kadvkWXwOruhbhd9D3Oja0OL4jhRJ2I1FNo3cY2yeQtgfL5Zbnucqa8sXm\n" +
                        "0/kuH3mkH9yhH4idMVOmdeurIYnhk7vObI3gA5XOE1L+tkUvf1JtUs7JYmuiPBaHLklDHxkOj6mv\n" +
                        "w0g+xUffDfurHdbfHY7vUNjnhIDXPPJ/MqQLHxuZgu6mnkOCxX0yhPo4shxZGjxB7VVlNXv1Np1j\n" +
                        "mFh6nCPj+i8Dajfa5WGX7L1M1z4oz0AknhS1qKZlRC6KoDXsIx0kd1oPdvYGlvMs91sjhBPy4s7A\n" +
                        "rZj2wkuMzuE012bL01uVpu+MZ8NXMJk5Az2+izGGpiewGOQOBC+eF609qrS9bLDNFV0zY3cPaSAf\n" +
                        "quaz7h6ntvyxXWqcB6PkJ/5rRPxsJ9wZ18cZH0O8xgGS5n71wz11NTxukklYGN78qBz94dXyM6TW\n" +
                        "vx24cVjt21rqO5PLprtVtB7tEzgP6quvxb32yI4xOHUO6elbOyTzblA6Ro/0fUtL7ieI2SaCSjsF\n" +
                        "MI5MHEoJwo2S1VRMS+eokkJ9XOJXXY7JIJIHut9Xi609tmiGPGPbPe1NqW96iqDUXOuklcT90OOF\n" +
                        "4fOerqAV0EUkz+iFr5f7oWb6H2x1LqitbFBb3wxHvI7svRUqMeJ23GPowqkp5qipZDAwvc84H1Kk\n" +
                        "TsPslPPUNvmoIMN4cyMjg8rYW1Wxtn0y+OruLW1VY3sTy0Lc1PGyCJsUEYDGjAA4XiZfkZTeo+jP\n" +
                        "Za/w4rbRU1JRR01OwRMY3AaBhdjh54acBcowR2CAY9F5De3tmTe2XDsiBFyQEREAREQBERAEREAR\n" +
                        "EQBERAEREAREQBERAWtVD6q9Wnso9kfppXxXUQqdDmQDLosn+ShW/PQR7jhTt8SELZdv6s+oaVBR\n" +
                        "3V8wA4GR/NfS+Lb+PR6VH+TY3h3ujqHcyjAd0xySNypy1kQrbZNEORLGQPqvnrtvM6n1lQTMJ6g4\n" +
                        "dvxX0KsRcbVSvfzmFp/eFk8pFQmmiq5KL2j5+7rUTqDXdypS0txJ/wAysX6i2QOacdLgW/vW/vFz\n" +
                        "pX4G9U93pYcNlJMjgPoo/DLmkHghenjS+SnSNFb5RJpeFnWhvmlDbquTNRTOEbG/7OFn259rZfdG\n" +
                        "VlI0FxlYQAob7Bavl0xrCAySFtPK4NcM8ZU6bdJBWUPnxObLDM0FuORyF4WXW6LORisjwls+bd1p\n" +
                        "jR3WppDlj43vZj8CQFIHwsblijmZpu4zhsb+Gud6ELG/FJolmn9S/aFHCfJlJc4gcZJytN2+pko6\n" +
                        "xlZTyujcHBwwfXK9ZQjlVbNWlOJ9M4pWyNa8EHqGWkeqwHefRtNq7TMtMYx8TG0uY7HP4ZWCeHnd\n" +
                        "+G9UzbNe5BHVxtDY3OI5W9mETxNLXNc3v+K8Nwljz2zFJSrltHzjuVJdNKagliIlp6mGT8MgFb92\n" +
                        "a37EMDLfqRwazhrZPvFbA342lpdV0ctxoI2xXBg+UNHdQ7v1kuViuT4K+lfTyNJbjGM49V61SryY\n" +
                        "a/TTHhNd+z6JWLUFqvVJHUUFZDM1wyMOGf3L1SGuOXHI+i+dGlNb6h01P51qrSyNp+YP5H7lt3TP\n" +
                        "iQu1HGxlyp/igO5Zx/VY7fGWRfRw8f8AUSkvum7Reoyy4UkUoIwCWc4WstW+H3S94Y74RwoifVrV\n" +
                        "52n/ABIadrS1tRb6inPYl0jcLN6Dd7RFS0OkvNPT5HZ5z/RUqvJq9HHGcfRpO7eGOogz9nVpnH1y\n" +
                        "F4Uvhx1Mx3yxgj++pKt3R0I4ZGo6U4/FdSs3d0RE1wZe4JXD9VpwSrVkZD/DpTsZH6i8N19lePiJ\n" +
                        "WxN9fmysusXhjoGObNX3MyYPLA08r3794itNUBeyCklqHDs4PCwG++Jepna9tto3xO9Oogq2P8ma\n" +
                        "6OlzZuW07V6E03TB81PACO75CB/VdG8bq6E0pG6CkqGN6eC1jM5x+CizrDdTVeomuZV1j2RnsGnC\n" +
                        "waWeWoJdPNI4/UrRX4+U+7Gdxqb/ANEo7x4l6OCUi30DpmfXjKWbxNU8s7Y663mFpPcHKi0Ccfe4\n" +
                        "9lQdLjnqIIV0vG1v0WfBHR9DNB7g2DVkGbZUjzW/eY4YWXiQHP07r587P6lrrHrKkLKl4jkeGlue\n" +
                        "/Kn3QymooYJT2e1pP7l42ZjfCzFbXx9Hfb2yiDsixoqCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIg\n" +
                        "CtJw0lVXDWyeTSSS9PV0tyAoiR+msPEdVxQaBqWyODXSNIblQXeel0nOckj81v3xJa1vd8mFoFG6\n" +
                        "KCBxyRnlaOp7dW1lU6KKA9R5H1X0/jdQq2z0KnqJkmy9tkuev6CjY3OXjqPsvoFboTDb4YT3jYG/\n" +
                        "uCjh4Xdr622TN1Bd4S17+YshSXOcdIXk513yTM+TIwDezSDdWaQqaNjSKgDrjIHtkqCN9ttTa7xU\n" +
                        "UNUzola8gDHdfSx7A9pa7tjBUXPFFthL1C/2mDgHLyArvH5fB8WWUW66I2RSOhkaWuIex3Vke6mF\n" +
                        "4X9wobtYYrHW1Dfi4h0syeT+KhzJ1tkcwtw8Ow5e5ojUVXpjUUVzo5HNLOSAV6uXQsmPRouq5LZO\n" +
                        "PePSEOrtLz03QDUsaS0gc+qglqezVVhu8turY+h8bvl47qeu1+tbZq+wx1VJIHShgEg+uOVrDxHb\n" +
                        "TR3emkv1pj66qMZLQO/fK8rDvePPgzNXPi9ET6GtrKGoZWUlQ+OZpBy04Kk7sbvnTywRWfUMjI5G\n" +
                        "kBspPcfVRgq4JaSqmgmHQ+M4IK4m9Tf0gcc4yC08r1r8eOQtGpxU4n0uoKujuNGKqkmbNHIOHtOc\n" +
                        "LCN0NsrDrC2vL6drKvGGyxt+bKixthvHfdItigfO6ppvSNxJKlHt9uvp3VNO1zKuKGqIGYC7nK8S\n" +
                        "zGtx5biY3U4PZFPX+0OotKVL3Npn1MHdrmNycfVa8nZLTvdHKXCQHBZjsvpVJBS1sBbNHDKyQchw\n" +
                        "BWrda7F6YvkstRSQCkqH89WPVbMfyjiuNhbHJXpkI5OkNB+YkjuPRWhvy9Rf1D2ycremsPDvqC2G\n" +
                        "SS0E1Y79IHqtb3DbrVtv6jUWqSOQehBXo15VdiLo2JmKMc8D77gPxKo4F3GS78SV7LtOXph/SUbs\n" +
                        "/guWDSN+qSBFSOye3C7U6V2S3E8IYxhjh+CAtD+nPQ7291ndp2j1xcpWCC0SCM95OkraOlPDTcJm\n" +
                        "Nmude6E55j6QVTLPprOPlUeyOoY9z/kPUfovUtOmr7dHYpLfPJn2aph6c2B0lbiHVtIysd69Rx/R\n" +
                        "bIsembHaIuiht8MWBgZYFit8xH8K5ZSIW2TY/WdzY1/wohaf2gcrIovDlqYwdTnNDvbCmMxjG5Hl\n" +
                        "tH4BXNaDkYOFjl5ObfJFX8nbINVO02p9Nantb6qnDoTO0l7QeAps2aMw2qmjOS7yW8H8FW4W2krw\n" +
                        "xtRE1wY7qBI9V22gNb0j0GGrJk5Msg4nPkczewRUb2VVQioIiIAiIgCIiAIiIAiIgCIiAIiIAiIg\n" +
                        "CIiAK17WuaWuGQVcqHOOEB49fpuzVpJnoKZxPcuiB/5Lp0uitP08vmR2+lBH/st/6LIx1eoQg9xw\n" +
                        "ulKS/Sds44YYoYhFFGGMb2AGAFzAcK3Dj64Vw7Lkgo4DHPZdC70MFxoZqOZrXNewjDhkLvv+6rMD\n" +
                        "q6sKVLj2SuiDm/e2FTpW7S3ClY51NM4yEjsD7LU3UcOJxjC+j2udNUGprLLbq2MFrwcOxyCoL7ua\n" +
                        "IqtHalqKd0L/AIVxPQ7HGF9H43MVi0zdRb/0t2u19ctF3eGale40rnfO3PGPwU19u9Y2zWdlNVBJ\n" +
                        "G89P6Rn/AML56gjy+Rx7LPNm9dVmjtQQGOZ3wkj8PaTxyVGdicvtEW177RvLfvZdt3Et9sUbW1Ds\n" +
                        "l0bWgD9yi3d7ZWWmqfRVkbop2HBBHdfRnTlzp71aaaup3NfFK3t9Vh24O0mnNViSSWBsFTID+kaO\n" +
                        "yx42b8UuMziu3j0QM6R1t+Qh37S7NBWVNI/zKSWSKQH7zXEFbT3G2O1Lp6qc6ihdVUeeHh3OPwWr\n" +
                        "q22V9C4tq6aaLpOPmYQvbruquiaFJSNo6A321Np3y6apeaiAdy89R/mt2aZ8ROnK+OOOvaYpj3Po\n" +
                        "ocBzc9uoq7HqwD8lTZ42u37Ih0KfZ9B7TuPpS4NaI7zSNe7sx0gBXvgWu4x+YxsM4PqGBy+bcE0t\n" +
                        "NMJInSNeOzyeyzTS+6WqrGQI7lNLGP1eohYLPFtf5ZTLHa9E7vsS0EfPbqU//wAm/wDRcsFktLPm\n" +
                        "jt1M0+/lj/otIbPb62+9ytoL2fhqggAZ5z+a3xRzRTRskid1RvGWkHIXlXQnX0zPODRyRU8UTcRt\n" +
                        "awezRhckaZ9grmdu2FmUtle+iuAmAiKdEDATCIgGB7JgeyIgCIiAIiIAiIgCIiAIiIAiIgCIiAIi\n" +
                        "IAiIgCIiAIiIAiIgCIiAoeytV5VMKGtg4njPIOCFge7mhqPWOm6iN0LRVBvyuxys/fGHY5xhCxpB\n" +
                        "yPxVlc3W9xEG0fNbU9lrbHdpaGrhczoeWgkYzg4XlnOT0uwWngqU/i20cySmjvdFC1jmn5wBxj1K\n" +
                        "iwzAe/BGM85X1eFZ89e2epW+UeyXnhF1LNW2eW01MpkdEMtyey3+1pA6SM/VRb8GlHOa+4VHSfhz\n" +
                        "GOl575ypTMblg+Y9l83nVpXdGG1al0cM1PDMOiSNjx/tDKxbU23mmb8wsrqCIE+rW4WYBjcdvzQM\n" +
                        "AVELJQ9MrU5IjZrLw20sk757NVMgZ6dYJH8lqPVmy+rLM58kVK6rjb+tE0j+qnc5jek5GVxSQRyM\n" +
                        "LJGh7D3BHC3U+RsgtMthfJHzSuNuuNDK6GspKinePR4z+a6owG4yc/UL6Gak280veYniotcAkcMe\n" +
                        "YG8haJ3V8PQp4HVWmnzSSnkMeB0j9y9SjyUJdSNMciP6Rtp5Z6dzJaaR0crTnIKmN4VtdVGotPSU\n" +
                        "FWXPlpuOoqIl4tlbZqs0twidHJGSDx6qSfgzs9XHTVlxkY+OFx+Xjg8rjySrcdoXaa6JNZ+XKuZ2\n" +
                        "VjeW4KvaMBfO60ef+lUREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAERE\n" +
                        "AREQBPREQGovEfWwx6OfTOaHVE2WMb9TkBRs0TsxqnUVQx9Rb3wUxdzJj0U4qy3UNaf+00sUuDx5\n" +
                        "jA7H71zU0ENO3y4WBjfZowFsqy5VQ1Euja0tGJbX6LodHWGGgp2jzcYkcB3WZRHLc+nogx7BXN7L\n" +
                        "I7HZ2yqTbZVERQQD2VMKqINnG8fTKsexrmfP2+oXOqJyaGyPG8+1Ump9cU1RQU4bTkjzMDg+63No\n" +
                        "bTlJpqxwWylja3oaPMIHqvf6RnPSM/ggHKtsyJTWifkbKtGFcrVUeqoTZGyqIi6AREQBERAEREAR\n" +
                        "EQBERAEREAREQBERAEREAREQBERAEREAREQBMorXZxkcqGAHj3VchcEkjGNLpOlv4kBdGa+2qA9M\n" +
                        "tZCD/fCmEZNE6Z6vUFRr2nOD2XSguFHUtzDUwuB9A4crnBHV1lvT/wA0UZJ9jTObrb7qgkaRnPC4\n" +
                        "nuYT872g9wScK3zGPdgvjOPQOHKaeyNM5xkjJKAEevCq0549FRxwDxlQtroDIwTlGPaT085/BcRm\n" +
                        "ixlssQA7jqCua7rZ8jmn8DlQk0ScvUOeeyBw91xOc0DnAb6klcfxEX9pF/EF1xb9BRbOySMd1QOH\n" +
                        "uuuKiJxwXx/xBXh2fu9JH0KiSaJ4f9OTrGcZVQ7PqrCchUe5rW/MWt+pOFHJNdFa/wBaOT809FxN\n" +
                        "liPHmRk/RwXJnPB7qe0zvRVVaeFQK0kDPOCj3vo5Xsu6xnCqHDGfRcHnMIIMsWR/tBVa7PJc0s+i\n" +
                        "lJnWjm6hnCquOIN5wchcihPZAREUgIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgC8fVV5p\n" +
                        "7HaJrhUyBjI2k8lex6LUfikiq5duZPgi4PbJl+P2cLuuPKSR3Wty0aN1nu1q3Wl5ltWnnuhHmdER\n" +
                        "hJyeV6tv2d3RraQVFXf5WPcA4DqPqsN8Od2tls1tHJdehgfJiMu91NylnhqKZssDmyQkAggrdlJU\n" +
                        "JFstRIc3GwboaLvlGay5VU0PnMB6CcYJCmDYnyS2akklcXO8oFxd3JV9dQ0ldEGVcEUrMggEZPHZ\n" +
                        "dhjGNiDOGNxgBYrrVKKkVOezUPiWkulu0h9rWy4VFK8O6SIzgLUnh6vWqdSaw6ay+VskELwMdXB/\n" +
                        "Fbm8TrOnbaRrvuB5/otMeD8gamrm5yPNGP3LZXBOnkWx1ol/E3oYBkkgYyVxV8TpqWSOOR0bnNwH\n" +
                        "N7hcyo/twvPUuyiPshXvFfNV6c3Bns9Nf64Qkhwy7nkqTGyMNcNFU1XX109VNOwSZlOcfQKMnib/\n" +
                        "ANcpe44AYz+qlNs+XnQluJ+78OMLfbWvh2XSj9Sm7dJXT6Rqn0FdPTSsYSDGcKIFl1BuBetQOtFt\n" +
                        "v9UZRL0fO/64U0Ndj/uxWHq48sqHWzIa7dx8jz0gVJ/4l3gwUots6h1EzWv0PvVbqN1cbzLKGclg\n" +
                        "cckLj0DvffrHfGWjUzW9DSGyOfnq7qVwY10TQ/lp9+yhZ4n6Whp9yyKSNscryMNaMZOVOPOFjcJC\n" +
                        "D5MmXY7jT3a0U9xpSHRzsD2fgsA8RJuNNomqrrdX1FLJDGXDyzhdvw+sqo9u6D4xxc7yx059ArfE\n" +
                        "JkbZ3UdyYjhZY1xhdorjH7keNh79q3U2tYaapvlY+Fgy4B3spkUzXMjZG5xf0NHzHuVDXwhf6w52\n" +
                        "DkNiJ/kplt5IeDwQpzYKEuibo8TkXnaleGWWqk850PTGT1tPIXon7pK1Z4itSmx6KfFE8tnqCWj8\n" +
                        "MKitbaKq1tkYNWa71bHqK4m23uudSwvPIdwpH+GTW02qdJGOumD61vcE+g9VrjbvbttdtHW3GaES\n" +
                        "VdZG94cRz6rFPDdqX/JjcB9rqz0NkkMIGfUnC9KdClXtGpw+pNSMYAauRcUTmuDXNOQeAuVeSlrZ\n" +
                        "kCIikBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAXl6ltcF2tU1FO1pErS3kZwvTBz2XHI\n" +
                        "efQ/RRyce0N67IWbvbMXzS9fNcbVFJVU0ji4CPu3n0x2XT0HvNqbR2KC4GV9O3jpkBJ49OVNqaCK\n" +
                        "eF0UrWvb6hwytca+2j0xqOmlaymjpp8Z62t9V6McyNkVCZfCe12dba/efT+rY2QyyCmquzWuGMra\n" +
                        "LJYpWNka4OYRkEKAm42krztzf2NbK5jXOzDK3jIUofDTq2p1FpMwVji+WEYymVhwUOUSZVdbOx4n\n" +
                        "c/5tps8/Px+5aV8Hv/iit/3o/ot0eJnP+bOU8n5s/wAlpTwfysZqmrDnYLpgGj34UVr+nR1H/BMZ\n" +
                        "UQcko3uvMlLejKiEnic/1zkenSz+qlZtB/4Btn/64UUvE6QN6CP9ln9VK3aIgaAthJ//ABwvYynq\n" +
                        "iJpk91noa/H/AHUrP92VDHaCYQ7ozyhheGVJJH+JTN1+4f5KVf1jKhts2Cd2ZJOkDNSQ5v06lXh/\n" +
                        "5Yxzc+4HiCorTJJaaGjkdV56ACD37LCdE7ZXzX2qmam1E50dMXB7A4+mV2vE/t8aOoj1Pa6f5GuD\n" +
                        "pCG9ifoso8Nu50Vzoxp65FraiEdLR2XSglByidJ+0b5tFDBbbZBRU7QI4WhowO6wjxBf6sLn/cKz\n" +
                        "+JwMQPpjP5LX/iBc3/Ndc3enlkrDS27ezNCP3I7eEH/WNU/7p39FMzsw49lDLwg/6xKh/oYyP5KZ\n" +
                        "pzjjur82W5ItyCxzg2NhceMZJUUPEBqSm1TuXR2FlQxlHTODZXOfj5sqR+4t2Fn0lcK3qDfLiJBK\n" +
                        "h5ofQdfuRfrhdIpiwOqCC/6qcaC3tiqPRK7TNy0ra9N0ttju1CGNiAx5jfZRL3hoqLTO5Hx9nq45\n" +
                        "ozKJiWEcHOfRbKHh7vAYA+6yn/EViO5+yd10/YpLi+pkqiwZcS4nDfVbcX402my3/wCEoNob+y/a\n" +
                        "QpakO6neWC4591mfdRb8H+s2iSSwVcvT5gPk5HcNUoRKwMaST83bAXnZFfCb0ZrYaZyDsUQFMhZm\n" +
                        "tlZUIgRSSEREAREQBERAEREAREQBERAEREAREQBERAERUPZAB64WFbj6wi0fHDX1UJdA44c/0CzU\n" +
                        "duFiG6ukxrDSs1pc4t6jkEd8rqGt9nUUm+zn0/rXTl5po56W5RF0rQenqHC9Gru9qhic6SthbgZP\n" +
                        "zhRam2P13aHv+ya+RjAflIJyqjavdGtAiqLpMAeD8xWr+PW/tssUUmdTxSartupr5S0NqHnuhy09\n" +
                        "Azknhbf8LumKmxaMZU1DS103JBHK6G1uxFBZ6htyvsklVVg56X4IJW7aWlip4WwwNbHG0YDW9gpy\n" +
                        "Ll8fCsmyz66Rh28VkffNDVdJDyRlwH5KHW0l7l0XuDEbj1QxxznrzxnlT5mhjfCY3gOae4Wkd4Ni\n" +
                        "aHUsr7jaHugqXO6i1owMrnFujGHGZFc0o6ZtW06ms9ypGT01bDIHNDvvDjK6eqdbWGw2moqqiuiD\n" +
                        "2MPSwO5J9FGql2j3Pt+aeguMkTB2wSsk0xsnqS51kUuq7nUPhY4F7Qe/KSrrS2GomjtwtSSam3Dd\n" +
                        "cn56QW4z6jKmztGGO0NbnMPDoBwtMbg7AVVw1EJ7KGw08bGgdI74Pqtz7T2S7WLTUNtuZYXRNDWl\n" +
                        "ueyvy7a7K0onblHjo9HcFxbpSscSABGVDjZlzTu11ZADqogfxKXm5tsu1401UW61FvXI3pJPcLRG\n" +
                        "lNjdUWTUVPd2yNcY5A8jnk5yqqLIwg0yK2ookfeLXSXe2S0NZA2SN7COQoYboaQu+2etBd7d5ooz\n" +
                        "L1RFoOCptUAqPg4/iQ0SlvzBvZeLrnSds1ZZpbbXxAZB6HgctP0VVVri9P0cwlp9mGbJ7n2/Vlmb\n" +
                        "FVTMiuEYDHtce5Xd8QZa7bG4uc8ZERwAe61XTbCahsV7+MsVymDA7PJ+9z6rYmstKas1Bog2WYwe\n" +
                        "Y6Poc4Z6irGoKXKLJTXLZofwiyMG5NS6Rwa0x4aD74UzsdQ4OMFRr242U1NpTUsFxjla5rSC7Pqp\n" +
                        "FVQrPs1/kdAqSzGD93K4yJRnLcSLWpeiP3i21g5tHTWC3zfpJpeiUNPos98PGkYNOaIgeQDLVgTu\n" +
                        "P1WtdV7Matv+qH3qrlaXdYLYxnpABUgNGUNTbtPUtFWtZ5sMYZ8vsolL6aRPJKGj1+zR27rytXWu\n" +
                        "K8WGsoHRB/mRloyPcL1+hpx34VJGu6H9BHVjIz2VMW4y2Uxk0yBVukm0HusGlxjbHUdGO3BOFOLT\n" +
                        "1fHX2qnq6eRsjHRA8HKj9uPsfftT6nqLrFIyMPf1Dp9FszZfTGp9M0H2fdpmywN4DjnK13WQmtot\n" +
                        "lNM2TB9zq9Xcq9UY3GRjHPCrhYN6ZQ1suHZECLokIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiAKhG\n" +
                        "RhVRAU7BdSpraeB4ExwcZXcXFLFE85exp/EKGtg6H2pQE5MuQqtuVAeesNPou18NT/2TP4Qnw1P/\n" +
                        "AGTP4Quu9DbOsLrRZ5eq/atEP1wuyKan/smfwhPhoP7Jn8IURWgdYXWiIP6QY9lT7Uov1n4C7Xw1\n" +
                        "P/ZM/hCr8NAf/KZ/CFIOj9p0GeoSBH3Sha0kSZzxhc1a6hpKd005hhiaMkuaFx0Uluq4GS0oimif\n" +
                        "z1tATbZPZZHcqFrekS4d6q9l0oW5IlySuCruNlpK5tNUTU7ap33WEDJXLXVFspIhPVvhijfwMgDl\n" +
                        "Skx2Xm60Az+mDSVT7WosdIlz7rmgjpZYmvjjikYeWnpHKuNNT46BHG0f3Que9kbZxNutC7tOBj6q\n" +
                        "gulH0lxkBHurLhJbaCAzVnkQxt/WdgBdCgv2nK+c0tFcKKok/Ya5uQu9dbJSZ6P2pQjkzc+yp9q0\n" +
                        "PfzMFc8dPTHJEMfUPXAK6N2uFotkRkuU1NS47OdgLmPJk9nYbdqIg4k490F0oekjzQQujabrYbrI\n" +
                        "51uqqGqkx92OQH+QXqspoeAYWDPcdIUS2iNnX+1aPB/S8+gVzLpQ4P6UA+q7IpKf+yZ/CFX4On/s\n" +
                        "mfwhRGWxs6xu1CP/ADWqn2tQjnzQ5dr4On/sY/4QnwdP/Ys/hC6IOoLrb2j/AEuMrnpquCp/0b1e\n" +
                        "aOnx/omfwhXxwRx9mtH4BNgvZznJyqqo+iLhrYCIi6AREQBERAEREAREQBERAEREAREQBERAEREA\n" +
                        "REQBUKqiApj6Jj6KqKNAtA+iuwiKQMJjhEQGuN/ZpYdC1bo858p3IOMHBWMbZaqi09tGLlWzjqZG\n" +
                        "MNceS7HCyTxB4O39YM8dBwfryow0k9ykhscF7dJFZy8dRHY9sZW/FqjODci6uO12bX2rtF619rKX\n" +
                        "V91dNTUsMn6FpJHUM+yy/wASsklPpWgERdH/ANpA6gfothaNhoIrHStt7I2weWMFnqteeJ850rQ+\n" +
                        "4qxn9yqT3Zof+tGw9CF7tJ0DnH5vJbyfXhdzUNzprRaaq41TgGU8ZefyC6WhXY0pb/8Act/osP8A\n" +
                        "EbNPFt5WmmLg4xODse2CqWvsccezW1A3U272oJXtrn0dljdzjI6h6cLYWmNnrfp66xV9HWv8wYLn\n" +
                        "HJyqeG+KlZoGlfAG9b42mUjvnC2qMeq7tnx6OpNp6MW3A1LBpTTE1fUPDXY6Y8D7zlpXSOkdS7nS\n" +
                        "SXrUlZLT22QkwRhxy5ufZe54o5pxDbYHFwpXTAux2ytr6Bhp4tKW0U4aGCEdPT6ruMlCO17Je0jH\n" +
                        "tvdsaTSFwdVUVUHBwxgtWwWtcHZIzn1yr2qqzyk5eykpynKqgVaBTlOVVF0CnKYVUQADCIiAIiIA\n" +
                        "iIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCIiAIiIAiIgCeiIgNZ+IL5tA1gGA7y3EA+/Kw7QW\n" +
                        "k6XVWzzKOpgDqjyg6N+OWuAyMLdF9tNDeaR9JX0/nRuBGMe6pYrTQWmgZR2+IRQx8YV9V/GGi2M9\n" +
                        "GjNj9VXLTuo59G6h8x5D8QPd3xlZH4onOZo2knEbnsZUhzunv2WxarS9kqLw26vomfGt7OxyuzfL\n" +
                        "FbL5R/C3KmbNEDktd7rmNnewp9mstKbu6Wo9P0dNMKkSRxAOGAssqJrbuFo+tjo2vDJonMHWOeR6\n" +
                        "K9+2mjg4f/aYuk8dysitdrorTSMpaGERxg8BoXPPs559kddvdRXLa2+VFkvtFO+3E4ZI0cgDstuU\n" +
                        "G62mLhWwUdNM980uB0juFkWo9O2W+wvhutHDOzjh3GfzXi2TbPR1orGXCgtTIqoHIcHE/wBV3OcX\n" +
                        "HbJlL9ODeDS7NV6WfBG4NmiHmRYHOVrXarcqo0vTHTuraaSJtLlkU4HcZ9crf7h1OID34HBaGheF\n" +
                        "qLRemr8wi52yKXPf0P8AJRG2LjomMkzp2HcmwXm5NoLc6SeVw7jGFmQdlwHAPqPVYppbb/Sump/i\n" +
                        "bLaYoZj+uXkkfvWURN6ZCS4ku7+wVbKjmQIgXC9gIiLoBERAEREAREQBERAEREAREQBERAf/2Q==\n",
                "iVBORw0KGgoAAAANSUhEUgAAAPsAAADJCAMAAADSHrQyAAAAkFBMVEX///8AAADR0tRLS0zIycsG\n" +
                        "BwnU1de4uLnq6ur7+/vw8PDk5OT8/Pz29vbKysrW1tbe3t6ioqInJyeoqKhaWltFRUUhISJlZWXB\n" +
                        "wcEODg/Nzc1vb2+GhoYxMTF6enqQkJAXFxc6Ojqurq9zc3N/f39ISEhnZ2gdHR+ampozMzOOj5GF\n" +
                        "hYVVVVUWFhdfX2E9PT+fUQHAAAAI2UlEQVR4nO2c6WKiOhSACQ5lCRAWhYgsghveor7/292cBLTt\n" +
                        "2E477VSZOd+PVtyaj5PkJCFU0xAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAE\n" +
                        "QRAEQRAEQRDkX8e6dQFuhecfScedWxfjBrg/FgRIuX3ronw3bJUQxXKR3bow34nNtlK7Xs/k76nu\n" +
                        "3bpI34Trl4Y0jqlmhRNpv/o3ar6+lrW9jmNZ12lYwOGu9G9dsD8OTQ+gGrV8asyPDJ5ywgiemnfs\n" +
                        "yfssmf4cepNC/gnsrJQ1fJMHGm2nS1HXM6jrTqtq/sI/t3seiTNBj+0NS/ulZI8HVdtZOwstzeHV\n" +
                        "nizXAbxE472Mfen37T47FNzqSHzL8n4dbKuqdpg5mmPWhQipa5kdiR7l8MYK5/Bykqp2n+1IcSJ/\n" +
                        "hbuXpbJWnyovJiQXzZjmRxOknWpGKjW2bVXG66DmC3dg/O62vzKIyGuzEKTNcr5sxQPLbBi0b4+X\n" +
                        "k1bV/FDYG9DuXZb8He7ZKlJ9e6COvSAuikpoe5YazdtBs4rlwyHjrdq/wd1mU2lRhJ6TLtgwhMkW\n" +
                        "p/jpYM7VG5XivLggF8bsLmq7dBCVOvA028zbYDC22oo/m8M6/StO2ImaLxi3u7+eq5hTEc8mcEXt\n" +
                        "9tk53E7Aro/jaVz39s23FfWLYansrHehDK/rUNmkXfjhOZT5AKPetZG8E0N7N8hE/8byfhlepuZq\n" +
                        "m/ZJbB0a8LbsEvKc4liG4iw47pPP2zDWE9X++GN0qxv+Qpa8iM8jcpuZ7fZk9LrGmfMZqLdVkz05\n" +
                        "UVazl9+x9d1rf+FeYaXMaqQKhmey6liTX1N0ZXyJM23VWG88qxvDykSR9xYOWy/Pdsuo7o6rKm5M\n" +
                        "Lpo7N5u4LdNuVuwuJ6CugsHfq+Q4n6RsDPN7t5+rzfqRjMtgsEZk1d4f85aza4uzHuNxu5icT1HX\n" +
                        "wsDWFWmPthv5zMq/e3t9LWt7Mozh+Lo+6wjtN1uuR3WzHN6ebEVitLgp7NX8flfed80P0iWEOGr7\n" +
                        "2LKtbLDkMI2t9y3J2R5tu77+Jytm88VjIFc3DPiW4NdfcCsYpCWDHMz+mEuFXRp+cPnFild9+E3N\n" +
                        "brrOtHgCX0xmdxx6GsuJ2Lzk8pCLx8na/41FWJs1U/gmGNd5TVQnEPc6pHed7axY9mo7uRQn4l5n\n" +
                        "v9tFOfqJSPdgobrAdgRXsMK97NUnvifcN58YlR1FnbezaZ827r6bl+TgDksQlSjzJ4IlrPOS9LX9\n" +
                        "64r3R8lJ2qhJuPGKux0852pM3SlRI94kvOP+/QWPZCVqfv2GO4uM+QUjut59T2XbkSs8oyEHdzFh\n" +
                        "795wfzaKT666Q9zJJBzXNE65iz7/1fbOImL87O7FYetrZhv2qzWiva/H0s4HBnfIca/HPdrmVVXl\n" +
                        "j6v54E7FaViDsSEPRdzHt27zLncxAqoXjZjYWMnZfQ7uIrMd5OEY3b0VSVXH/aa7qvR7h+6exr0c\n" +
                        "d9z1hETqwtLb7nLRZm5d3G2m61QLdF2tVo/RPdt9wH35xN11HDFh98RPdTg+dxtW5CtZ6T/obh2g\n" +
                        "vadiNiAPR+geyKwlR2IfdKdG39fN5eEI3UPZh8lCf9R96OuIPByhe7WLouggN0x80N2p8tzUmjzv\n" +
                        "d1uMz536me/7cjz2QfcXQNxjz7HGNagd+IW7bBxvu//3oOv6KOVfXbsIwP1QbNKcy3Hd9atux959\n" +
                        "bPKQpZ1GjNti8wrtjhjzzSrkPufxgSyra28yZ4P7w6jk7QCK3D6brT3DMOZJsjT6FY7X3tS76w8j\n" +
                        "msK7VFfun6V311+5UH+PWCpa8WzyOWZt755dX9a6Qzy958enGb5JD+56af6M/TAU+OEd6C/onw6C\n" +
                        "gAIWdJqe4NZW78INXuq8gjBkLFCSlkBK2rbtjiPE17CuST70q9FnSxlM8Byv6M94DESpiqWqrtJS\n" +
                        "ev5NogiC/LNYVKVh0dF95EMDlymLTe97m8HP8G4iN1zY9ez07jGo3816Tu3Zl03qcS3ZeAsyl1tt\n" +
                        "PDELebf79DJzWZ/d9YgcxzGS63G2/TYju3e3nfPuKk/kevHYsYYlKNsKAvlqu10sNoScVos0Vivz\n" +
                        "tuOxghzV9lrHcs6n0fbutiE4C7J8Gnf/sTttY7loZZanYrb+ER/3+6PcVk0fI0KSdb+pICakgpX9\n" +
                        "zdSCvZSbPChIseVwB1E6m+RqQ23Q5CV75U/fHBH3ndpgJd1NtV26E8HtJ/L9/unU0dxOPZwo+ZCQ\n" +
                        "XIjuyZLKBf5jAPs2TI2qO4wSaPtmTciB31DvTYS7MTsKoAm7bE+MyTaByw16QuYl3A1BiqrdE9LC\n" +
                        "yahNsxsu4PTuJ5IId0bIVrjPMs3OCYlSuG/Q0XTYcBjdtfuw2ETAZ61pWUQiR1VpuiGGKLu/IytN\n" +
                        "+IiIsx3ZyyZxzX1ra3RHIvG2FXy8JGTa3O9/CgD3JAKgzi+I4Ys2vCYHP5QXGZxUXqNkNVkIy906\n" +
                        "zx+XhMiBwDV30c/DI00+cfSmpLjjTZXgvmwcmL0Jd1HYOTTmVuS9UN704xwH91WwP+c12eBfcTfl\n" +
                        "0xotyNSakc0977Z6luO8I5mLXtmtyJz37iruuop7AovXnHOZBF9x9+H6nOj8EjKl9+++vOT3Ejpq\n" +
                        "GO8s9WdxfxDudidPjMZNPvR1/4k3zKS76OcXyl08mopXM3EyRuWuNTKZmXNSuO0L961XifTGs0eD\n" +
                        "pE/i7kGn5sFPyO8i1VsFJE3IhzE93b37pc67lijyTuRkEmrP3EXu2zp2rVr7XvVfyl0zYZM87Lin\n" +
                        "DPJ7LK9n70WajLTg/t2NJ2NaJscvu1y7uO+EuzWF+kDhHtkk7bvu3t02J/IWAVNz4F9ihJpTyTvm\n" +
                        "J7oW1GR2z+424/1dnqIP0+CO57aKYQ855Vy0Y1vnMj8HXLdhDZ/zbEjX4g1KjPpNk8F3WL76TBZW\n" +
                        "lUnhHwHx39mHf0O+Ym0S1zcRBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQBEEQ\n" +
                        "BEEQ5F/jfxiIqTwUGkcYAAAAAElFTkSuQmCC\n"
        };

        return img[i];
    }

    class JobListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        ImageView img;
        TextView title, companyName, workplace, workingDate, wages;

        JobListViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            img = (CircleImageView) view.findViewById(R.id.img_company);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            companyName = (TextView) view.findViewById(R.id.tv_company_name);
            workplace = (TextView) view.findViewById(R.id.tv_workplace);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
        }
    }
}
