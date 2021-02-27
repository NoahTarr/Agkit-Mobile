This section provides definitions and workflow information regarding the development branch workflow and the deployment branches.


## Branch Definitions  
*Deployment and QA branches*: These branches should only be updated by the manager(s). General pull requests for new features should never be made directly to either of these branches.  

**master**: deployment branch. The website is set up to automatically deploy all code contained in this branch.  

**submaster**: pre-deployment and QA branch. The working master for the team that should be used to branch off of for new features. This branch is the final step before deployment. All new features to be released at the next checkpoint get added to this branch so a final QA run can be accomplished to ensure all new features work properly together.  

**review-branch**: primary developer branch. All newly completed features, bug fixes, etc. should have their PR’s submitted to this branch.  


## New Branches  
*Naming*: The name of any new branch should follow this naming strategy template:  
> <issue_label>-<issue_id#>-<short_issue_description>-"<branch_creator_initials>"  

> New branches should always be branched off of *submaster*

Start with the issue label following the table below. Follow this with the issue’s ID then a short description of the issue, less than ~4 words. If the branch will be worked on by a single developer, then add the initials of that developer to the end. If multiple people will be working on it, omit the initials.  

|  Label Name  | Issue Label for Branch |
|:------------:|:----------------------:|
|      bug     |           bug          |
|  new feature |         feature        |
| optimization |      optimization      |
|    styling   |          style         |
|     misc     |          misc          |

> e.g. (Branch for single developer): feature-42-email-verification-NT  
> e.g. (Branch for multiple developers): feature-42-email-verification

## Workflow Order:  
1. When you complete an issue, PR from *<your_branch>* to *review-branch*. 
2. When the PR is approved and merged, delete *<your_branch>*.
3. When review-branch is verified the manager will PR from *review-branch* to *submaster*.
4. *submaster* will then be tested extensively.
5. When *submaster* appears to be working flawlessly the manager will then PR from *submaster* to *master* for deployment.  
> e.g. *<dev_branch>*  ->  *review-branch*(Preliminary feature testing)  ->  *submaster*(final QA of new features)  ->  *master*(deployed)  
